package com.example.lostintravel.data.remote.util

sealed class NetworkException(
    override val message: String,
    override val cause: Throwable? = null
) : Exception(message, cause) {
    
    class ServerError(
        val code: Int,
        override val message: String,
        override val cause: Throwable? = null
    ) : NetworkException(message, cause)
    
    class ConnectionError(
        override val message: String = "No internet connection",
        override val cause: Throwable? = null
    ) : NetworkException(message, cause)
    
    class UnknownError(
        override val message: String = "Unknown error occurred",
        override val cause: Throwable? = null
    ) : NetworkException(message, cause)
    
    class TimeoutError(
        override val message: String = "Request timed out",
        override val cause: Throwable? = null
    ) : NetworkException(message, cause)
    
    class AuthenticationError(
        override val message: String = "Authentication failed",
        override val cause: Throwable? = null
    ) : NetworkException(message, cause)
}