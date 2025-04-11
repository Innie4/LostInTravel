package com.example.lostintravel.data.remote.util

import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

class ApiResponseHandler {
    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Result<T> {
        return try {
            val response = apiCall()
            handleApiResponse(response)
        } catch (e: HttpException) {
            Result.error(NetworkException.ServerException(
                code = e.code(),
                message = e.message ?: "HTTP error",
                cause = e
            ))
        } catch (e: SocketTimeoutException) {
            Result.error(NetworkException.TimeoutException(cause = e))
        } catch (e: IOException) {
            Result.error(NetworkException.ConnectionException(cause = e))
        } catch (e: Exception) {
            Result.error(NetworkException.UnknownException(cause = e))
        }
    }
    
    private fun <T> handleApiResponse(response: Response<T>): Result<T> {
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                return Result.success(body)
            }
        }
        
        val errorBody = response.errorBody()?.string() ?: "Unknown error"
        return Result.error(
            NetworkException.ServerException(
                code = response.code(),
                message = errorBody
            )
        )
    }
}