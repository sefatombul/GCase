package com.sefatombul.gcase.data.remote.auth

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WoogletService {
    @GET("callback.txt")
    suspend fun getAuthToken(): Response<ResponseBody>

    @GET("callback")
    suspend fun setAuthTokenClear(@Query("token") code: String = "0"): Response<Any>
}