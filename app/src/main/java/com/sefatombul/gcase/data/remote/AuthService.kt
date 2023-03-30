package com.sefatombul.gcase.data.remote

import com.sefatombul.gcase.data.model.AccessToken
import com.sefatombul.gcase.utils.Constants
import retrofit2.Response
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {
    @Headers("Accept: application/json")
    @POST("login/oauth/access_token")
    suspend fun getAccessToken(
        @Query("client_id") clientId: String = Constants.CLIENT_ID,
        @Query("client_secret") clientSecret: String = Constants.CLIENT_SECRET,
        @Query("code") code: String,
        @Query("redirect_uri") redirectUri: String = Constants.REDIRECT_URL
    ): Response<AccessToken>
}