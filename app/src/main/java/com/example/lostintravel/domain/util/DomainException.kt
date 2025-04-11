package com.example.lostintravel.domain.util

sealed class DomainException(
    override val message: String,
    override val cause: Throwable? = null
) : Exception(message, cause) {
    
    class ServerError(
        val code: Int,
        override val message: String,
        override val cause: Throwable? = null
    ) : DomainException(message, cause)
    
    class NetworkError(
        override val message: String = "Network connection error",
        override val cause: Throwable? = null
    ) : DomainException(message, cause)
    
    class NotFoundError(
        override val message: String = "Resource not found",
        override val cause: Throwable? = null
    ) : DomainException(message, cause)
    
    class UnknownError(
        override val message: String = "Unknown error occurred",
        override val cause: Throwable? = null
    ) : DomainException(message, cause)
    
    class AuthError(
        override val message: String = "Authentication error",
        override val cause: Throwable? = null
    ) : DomainException(message, cause)
}