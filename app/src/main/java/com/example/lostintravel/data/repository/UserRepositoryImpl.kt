package com.example.lostintravel.data.repository

import com.example.lostintravel.data.local.datastore.UserPreferences
import com.example.lostintravel.domain.model.User
import com.example.lostintravel.domain.repository.UserRepository
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val userPreferences: UserPreferences
) : UserRepository {

    override suspend fun signInWithGoogle(googleAccount: GoogleSignInAccount): Result<User> {
        return try {
            val credential = GoogleAuthProvider.getCredential(googleAccount.idToken, null)
            val authResult = firebaseAuth.signInWithCredential(credential).await()
            val firebaseUser = authResult.user
            
            if (firebaseUser != null) {
                val user = User(
                    id = firebaseUser.uid,
                    name = firebaseUser.displayName ?: "",
                    email = firebaseUser.email ?: "",
                    photoUrl = firebaseUser.photoUrl?.toString()
                )
                
                // Save user to DataStore
                userPreferences.saveUser(user)
                
                Result.success(user)
            } else {
                Result.failure(Exception("Firebase user is null"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signOut() {
        firebaseAuth.signOut()
        userPreferences.clearUser()
    }

    override fun getUserFlow(): Flow<User?> {
        return userPreferences.userFlow
    }

    override suspend fun isUserSignedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }
}