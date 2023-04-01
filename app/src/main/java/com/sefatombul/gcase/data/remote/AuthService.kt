package com.sefatombul.gcase.data.remote

import com.sefatombul.gcase.data.model.AccessToken
import com.sefatombul.gcase.utils.Constants
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface AuthService {
    @Headers("Accept: application/json")
    @POST("login/oauth/access_token")
    suspend fun getAccessToken(
        @Query("client_id") clientId: String = Constants.CLIENT_ID,
        @Query("client_secret") clientSecret: String = Constants.CLIENT_SECRET,
        @Query("code") code: String,
        @Query("redirect_uri") redirectUri: String = Constants.REDIRECT_URL
    ): Response<AccessToken>

    @FormUrlEncoded
    @POST("login/oauth/access_token")
    suspend fun getAccessTokenWithRefreshToken(
        @Query("client_id") clientId: String = Constants.CLIENT_ID,
        @Query("client_secret") clientSecret: String = Constants.CLIENT_SECRET,
        @Field("refresh_token") refreshToken: String,
        @Field("grant_type") grantType: String = "refresh_token"
    ): Response<String>


}