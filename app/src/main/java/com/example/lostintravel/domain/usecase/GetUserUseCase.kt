package com.example.lostintravel.domain.usecase

import com.example.lostintravel.domain.model.User
import com.example.lostintravel.domain.repository.UserRepository
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(googleAccount: GoogleSignInAccount): Result<User> {
        return userRepository.signInWithGoogle(googleAccount)
    }
}