package com.example.lostintravel.domain.repository

import com.example.lostintravel.domain.model.User
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun signInWithGoogle(googleAccount: GoogleSignInAccount): Result<User>
    suspend fun signOut()
    fun getUserFlow(): Flow<User?>
    suspend fun isUserSignedIn(): Boolean
}