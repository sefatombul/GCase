package com.sefatombul.gcase.data.repository

import com.sefatombul.gcase.data.model.search.SearchRepositoryResponseModel
import com.sefatombul.gcase.data.model.search.SearchUserResponseModel
import com.sefatombul.gcase.data.remote.ApiService
import retrofit2.Response
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
        dateRange: String? = null,
    ): Response<SearchRepositoryResponseModel> {
        var text = searchText
        dateRange?.let { range ->
            text = "$searchText $range"
        }
        return if (sort != null) {
            apiService.searchRepositoryWithSort(
                text, sort, order, pageSize, page
            )
        } else {
            apiService.searchRepository(
                text, pageSize, page
            )
        }
    }

    suspend fun getRepository(owner: String, repo: String) = apiService.getRepository(owner, repo)
    suspend fun searchUser(
        q: String,
        pageSize: Int,
        page: Int,
        sort: String? = null,
        order: String = "desc",
        type: String,
        dateRange: String? = null,
    ): Response<SearchUserResponseModel> {
        var text = q
        dateRange?.let { range ->
            text = "$q $range"
        }
        return if (sort != null) {
            apiService.searchUser(
                "$text $type", pageSize, page, sort, order
            )
        } else {
            apiService.searchUser(
                "$text $type", pageSize, page
            )
        }
    }

    suspend fun getUser(owner: String) = apiService.getUser(owner)
    suspend fun userRepository() = apiService.userRepository()
    suspend fun getUserProfile() = apiService.getUserProfile()
    suspend fun getStarredRepositories(
        pageSize: Int,
        page: Int,
    ) = apiService.getStarredRepositories(pageSize, page)
}