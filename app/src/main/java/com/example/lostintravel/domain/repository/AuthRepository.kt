package com.example.lostintravel.domain.repository

import com.example.lostintravel.domain.model.User
import com.example.lostintravel.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: Flow<User?>
    val isUserAuthenticated: Flow<Boolean>
    
    suspend fun signInWithEmail(email: String, password: String): Result<User>
    suspend fun signUpWithEmail(email: String, password: String, name: String): Result<User>
    suspend fun signInWithGoogle(idToken: String): Result<User>
    suspend fun signOut(): Result<Unit>
    suspend fun resetPassword(email: String): Result<Unit>
    suspend fun updateProfile(name: String, photoUrl: String? = null): Result<Unit>
}