package com.sefatombul.gcase.data.remote

import com.sefatombul.gcase.data.model.search.GetRepositoryResponseModel
import com.sefatombul.gcase.data.model.search.SearchRepositoryResponseModel
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

}