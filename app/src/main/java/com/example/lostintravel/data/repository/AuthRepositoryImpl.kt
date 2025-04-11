package com.example.lostintravel.data.repository

import com.example.lostintravel.domain.model.User
import com.example.lostintravel.domain.repository.AuthRepository
import com.example.lostintravel.domain.util.Result
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override val currentUser: Flow<User?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser?.toUser())
        }
        firebaseAuth.addAuthStateListener(listener)
        awaitClose { firebaseAuth.removeAuthStateListener(listener) }
    }

    override val isUserAuthenticated: Flow<Boolean> = currentUser.map { it != null }

    override suspend fun signInWithEmail(email: String, password: String): Result<User> {
        return try {
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = authResult.user?.toUser()
                ?: return Result.Error(Exception("Authentication failed"))
            Result.Success(user)
        } catch (e: Exception) {
            Timber.e(e, "Error signing in with email")
            Result.Error(e)
        }
    }

    override suspend fun signUpWithEmail(email: String, password: String, name: String): Result<User> {
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user
                ?: return Result.Error(Exception("User creation failed"))
            
            // Update profile with name
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()
            
            firebaseUser.updateProfile(profileUpdates).await()
            
            // Send email verification
            firebaseUser.sendEmailVerification().await()
            
            Result.Success(firebaseUser.toUser())
        } catch (e: Exception) {
            Timber.e(e, "Error signing up with email")
            Result.Error(e)
        }
    }

    override suspend fun signInWithGoogle(idToken: String): Result<User> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val authResult = firebaseAuth.signInWithCredential(credential).await()
            val user = authResult.user?.toUser()
                ?: return Result.Error(Exception("Google authentication failed"))
            Result.Success(user)
        } catch (e: Exception) {
            Timber.e(e, "Error signing in with Google")
            Result.Error(e)
        }
    }

    override suspend fun signOut(): Result<Unit> {
        return try {
            firebaseAuth.signOut()
            Result.Success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "Error signing out")
            Result.Error(e)
        }
    }

    override suspend fun resetPassword(email: String): Result<Unit> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "Error resetting password")
            Result.Error(e)
        }
    }

    override suspend fun updateProfile(name: String, photoUrl: String?): Result<Unit> {
        return try {
            val currentUser = firebaseAuth.currentUser
                ?: return Result.Error(Exception("No user is signed in"))
            
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .apply {
                    photoUrl?.let { setPhotoUri(android.net.Uri.parse(it)) }
                }
                .build()
            
            currentUser.updateProfile(profileUpdates).await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "Error updating profile")
            Result.Error(e)
        }
    }

    private fun com.google.firebase.auth.FirebaseUser.toUser(): User {
        return User(
            id = uid,
            email = email ?: "",
            name = displayName ?: "",
            photoUrl = photoUrl?.toString(),
            isEmailVerified = isEmailVerified
        )
    }
}