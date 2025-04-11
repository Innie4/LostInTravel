package com.example.lostintravel.domain.usecase

import com.example.lostintravel.domain.model.User
import com.example.lostintravel.domain.repository.UserRepository
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(token: String): Result<User> {
        return try {
            val user = userRepository.signInWithGoogle(token)
            if (user != null) {
                Result.success(user)
            } else {
                Result.failure(Exception("Google sign-in failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}