package com.sefatombul.gcase.data.repository

import com.sefatombul.gcase.data.remote.ApiService
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject

class ApiRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun searchRepository(
        searchText: String,
        pageSize: Int,
        page: Int,
        sort: String? = null,
        order: String = "desc",
    ) = if (sort != null) {
        apiService.searchRepositoryWithSort(
            searchText, sort, order, pageSize, page
        )
    } else {
        apiService.searchRepository(
            searchText, pageSize, page
        )
    }

    suspend fun getRepository(owner: String, repo: String) = apiService.getRepository(owner, repo)
    suspend fun searchUser(
        q: String,
        pageSize: Int,
        page: Int,
        sort: String? = null,
        order: String = "desc",
        type: String
    ) = if (sort != null) {
        apiService.searchUser(
            "$q type:$type", pageSize, page, sort, order
        )
    } else {
        apiService.searchUser(
            "$q type:$type", pageSize, page
        )
    }

    suspend fun getUser(owner: String) = apiService.getUser(owner)
}