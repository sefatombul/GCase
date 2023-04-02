package com.sefatombul.gcase.data.remote

import android.content.ClipData.Item
import com.sefatombul.gcase.data.model.ProfileResponseModel
import com.sefatombul.gcase.data.model.search.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("/search/repositories")
    suspend fun searchRepository(
        @Query("q") searchText: String,
        @Query("per_page") pageSize: Int,
        @Query("page") page: Int,
    ): Response<SearchRepositoryResponseModel>

    @GET("/search/repositories")
    suspend fun searchRepositoryWithSort(
        @Query("q") searchText: String,
        @Query("sort") sort: String,
        @Query("order") order: String,
        @Query("per_page") pageSize: Int,
        @Query("page") page: Int,
    ): Response<SearchRepositoryResponseModel>


    @GET("repos/{owner}/{repo}")
    suspend fun getRepository(
        @Path("owner") owner: String, @Path("repo") repo: String
    ): Response<GetRepositoryResponseModel>


    @GET("/search/users")
    suspend fun searchUser(
        @Query("q") searchText: String,
        @Query("per_page") pageSize: Int,
        @Query("page") page: Int,
    ): Response<SearchUserResponseModel>

    @GET("/search/users")
    suspend fun searchUser(
        @Query("q") searchText: String,
        @Query("per_page") pageSize: Int,
        @Query("page") page: Int,
        @Query("sort") sort: String,
        @Query("order") order: String,
    ): Response<SearchUserResponseModel>

    @GET("users/{owner}")
    suspend fun getUser(
        @Path("owner") owner: String
    ): Response<GetUserResponseModel>


    @GET("/user/repos")
    suspend fun userRepository(): Response<List<Items>>

    @GET("/user")
    suspend fun getUserProfile(): Response<ProfileResponseModel>

    @GET("/user/starred")
    suspend fun getStarredRepositories(
        @Query("per_page") pageSize: Int,
        @Query("page") page: Int,
    ): Response<List<Items>>
}