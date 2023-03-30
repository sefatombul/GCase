package com.sefatombul.gcase.utils

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T: Any>(data: T? = null) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T> : Resource<T>()
}