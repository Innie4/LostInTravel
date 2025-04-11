package com.example.lostintravel.data.mapper

import com.example.lostintravel.data.remote.util.NetworkException
import com.example.lostintravel.domain.util.DomainException
import javax.inject.Inject

class ExceptionMapper @Inject constructor() {
    
    fun mapToDomainException(networkException: Exception): DomainException {
        return when (networkException) {
            is NetworkException.ServerException -> {
                if (networkException.code == 404) {
                    DomainException.NotFoundError(
                        message = networkException.message,
                        cause = networkException
                    )
                } else {
                    DomainException.ServerError(
                        code = networkException.code,
                        message = networkException.message,
                        cause = networkException
                    )
                }
            }
            is NetworkException.ConnectionException, 
            is NetworkException.TimeoutException -> {
                DomainException.NetworkError(
                    message = networkException.message,
                    cause = networkException
                )
            }
            is NetworkException.ParsingException -> {
                DomainException.UnknownError(
                    message = "Error parsing data: ${networkException.message}",
                    cause = networkException
                )
            }
            else -> {
                DomainException.UnknownError(
                    message = networkException.message ?: "Unknown error",
                    cause = networkException
                )
            }
        }
    }
}