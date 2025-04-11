package com.example.lostintravel.data.remote.util

import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

class NetworkResponseHandler {
    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Result<T> {
        return try {
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body)
                } else {
                    Result.error(NetworkException.ServerError(
                        code = response.code(),
                        message = "Response body is null"
                    ))
                }
            } else {
                Result.error(NetworkException.ServerError(
                    code = response.code(),
                    message = response.message() ?: "Unknown error"
                ))
            }
        } catch (e: HttpException) {
            Result.error(NetworkException.ServerError(
                code = e.code(),
                message = e.message ?: "HTTP error",
                cause = e
            ))
        } catch (e: SocketTimeoutException) {
            Result.error(NetworkException.TimeoutError(cause = e))
        } catch (e: IOException) {
            Result.error(NetworkException.ConnectionError(cause = e))
        } catch (e: Exception) {
            Result.error(NetworkException.UnknownError(cause = e))
        }
    }
}