package com.sefatombul.gcase.utils

import retrofit2.Response
import timber.log.Timber

object RequestHelper {
    fun <T> handleResponse(response: Response<T>): Resource<T> {
        if (response != null) {
            if (response.isSuccessful) {
                response.body()?.let {
                    return Resource.Success(it) as Resource<T>
                }
            } else {
                val responseCode = response.code()
                Timber.e(responseCode.toString())
                if (responseCode == 401) {
                    return Resource.Error(Constants.CODE_401)
                } else if (responseCode > 500) {
                    return Resource.Error(Constants.SERVER_ERROR)
                }
                return Resource.Error(response.message())
            }
        }
        return Resource.Error(Constants.UNKNOWN_ERROR)
    }
}