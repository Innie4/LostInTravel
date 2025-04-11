package com.example.lostintravel.data.remote.util

sealed class NetworkException(
    override val message: String,
    override val cause: Throwable? = null
) : Exception(message, cause) {
    
    class ServerException(
        val code: Int,
        override val message: String,
        override val cause: Throwable? = null
    ) : NetworkException(message, cause)
    
    class ConnectionException(
        override val message: String = "No internet connection",
        override val cause: Throwable? = null
    ) : NetworkException(message, cause)
    
    class UnknownException(
        override val message: String = "Unknown error occurred",
        override val cause: Throwable? = null
    ) : NetworkException(message, cause)
    
    class TimeoutException(
        override val message: String = "Request timed out",
        override val cause: Throwable? = null
    ) : NetworkException(message, cause)
    
    class ParsingException(
        override val message: String = "Error parsing response",
        override val cause: Throwable? = null
    ) : NetworkException(message, cause)
}