package com.example.lostintravel.data.remote.util

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    object Loading : Result<Nothing>()

    companion object {
        fun <T> success(data: T): Result<T> = Success(data)
        fun error(exception: Exception): Result<Nothing> = Error(exception)
        fun loading(): Result<Nothing> = Loading
    }
}

// Extension functions for easier handling
inline fun <T, R> Result<T>.map(transform: (T) -> R): Result<R> {
    return when (this) {
        is Result.Success -> Result.success(transform(data))
        is Result.Error -> Result.error(exception)
        is Result.Loading -> Result.loading()
    }
}

inline fun <T> Result<T>.onSuccess(action: (T) -> Unit): Result<T> {
    if (this is Result.Success) action(data)
    return this
}

inline fun <T> Result<T>.onError(action: (Exception) -> Unit): Result<T> {
    if (this is Result.Error) action(exception)
    return this
}

inline fun <T> Result<T>.onLoading(action: () -> Unit): Result<T> {
    if (this is Result.Loading) action()
    return this
}