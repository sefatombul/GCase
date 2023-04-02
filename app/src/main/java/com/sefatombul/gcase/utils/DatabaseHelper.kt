package com.sefatombul.gcase.utils

import com.google.gson.Gson
import timber.log.Timber

object DatabaseHelper {
    fun <T> handleResponse(response: T): Resource<T> {
        return Resource.Success(response)
    }
}