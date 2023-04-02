package com.sefatombul.gcase.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sefatombul.gcase.data.model.AccessToken
import com.sefatombul.gcase.data.model.search.*
import com.sefatombul.gcase.data.repository.ApiRepository
import com.sefatombul.gcase.utils.Resource
import com.sefatombul.gcase.utils.globalSafeCall
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    application: Application, private val apiRepository: ApiRepository
) : AndroidViewModel(application) {

    private var _searchRepositoryResponse: MutableLiveData<Resource<SearchRepositoryResponseModel>?> =
        MutableLiveData()
    val searchRepositoryResponse: LiveData<Resource<SearchRepositoryResponseModel>?> get() = _searchRepositoryResponse

    private var _getRepositoryResponse: MutableLiveData<Resource<GetRepositoryResponseModel>?> =
        MutableLiveData()
    val getRepositoryResponse: LiveData<Resource<GetRepositoryResponseModel>?> get() = _getRepositoryResponse

    private var _searchUserResponse: MutableLiveData<Resource<SearchUserResponseModel>?> =
        MutableLiveData()
    val searchUserResponse: LiveData<Resource<SearchUserResponseModel>?> get() = _searchUserResponse

    private var _getUserResponse: MutableLiveData<Resource<GetUserResponseModel>?> =
        MutableLiveData()
    val getUserResponse: LiveData<Resource<GetUserResponseModel>?> get() = _getUserResponse


    fun clearGetUserResponse() {
        _getUserResponse.postValue(null)
    }

    fun clearSearchUserResponse() {
        _searchUserResponse.postValue(null)
    }

    fun clearSearchRepositoryResponse() {
        _searchRepositoryResponse.postValue(null)
    }

    fun clearGetRepositoryResponse() {
        _getRepositoryResponse.postValue(null)
    }


    fun searchRepository(
        searchText: String,
        pageSize: Int,
        page: Int,
        dateRange: String? = null,
        sort: String? = null,
        order: String = "desc",
    ) = viewModelScope.launch {
        searchRepositorySafeCall(searchText, sort, order, pageSize, page, dateRange)
    }

    private suspend fun searchRepositorySafeCall(
        searchText: String,
        sort: String? = null,
        order: String = "desc",
        pageSize: Int,
        page: Int,
        dateRange: String? = null,
    ) {
        _searchRepositoryResponse.postValue(Resource.Loading())
        _searchRepositoryResponse.postValue(globalSafeCall(getApplication()) {
            apiRepository.searchRepository(searchText, pageSize, page, sort, order, dateRange)
        })
    }


    fun getRepository(
        owner: String, repo: String
    ) = viewModelScope.launch {
        getRepositorySafeCall(owner, repo)
    }

    private suspend fun getRepositorySafeCall(
        owner: String, repo: String
    ) {
        _getRepositoryResponse.postValue(Resource.Loading())
        _getRepositoryResponse.postValue(globalSafeCall(getApplication()) {
            apiRepository.getRepository(owner, repo)
        })
    }

    fun searchUser(
        searchText: String,
        pageSize: Int,
        page: Int,
        type: String,
        sort: String? = null,
        order: String = "desc",
        dateRange: String? = null,
    ) = viewModelScope.launch {
        searchUserSafeCall(searchText, sort, order, pageSize, page, type, dateRange)
    }

    private suspend fun searchUserSafeCall(
        searchText: String,
        sort: String? = null,
        order: String = "desc",
        pageSize: Int,
        page: Int,
        type: String,
        dateRange: String? = null,
    ) {
        _searchUserResponse.postValue(Resource.Loading())
        _searchUserResponse.postValue(globalSafeCall(getApplication()) {
            apiRepository.searchUser(searchText, pageSize, page, sort, order, type, dateRange)
        })
    }


    fun getUser(
        owner: String
    ) = viewModelScope.launch {
        getUserSafeCall(owner)
    }

    private suspend fun getUserSafeCall(
        owner: String
    ) {
        _getUserResponse.postValue(Resource.Loading())
        _getUserResponse.postValue(globalSafeCall(getApplication()) {
            apiRepository.getUser(owner)
        })
    }
}