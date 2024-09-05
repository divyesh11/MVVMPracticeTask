package com.example.mvvmpracticetask.utils


sealed class DatabaseResponse<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : DatabaseResponse<T>(data)

    class Error<T>(message: String, data: T? = null) : DatabaseResponse<T>(data, message)

    class Loading<T> : DatabaseResponse<T>()
}