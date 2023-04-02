package com.sefatombul.gcase.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sefatombul.gcase.data.model.AccessToken
import com.sefatombul.gcase.data.model.search.GetRepositoryResponseModel
import com.sefatombul.gcase.data.model.search.SearchRepositoryResponseModel
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
        sort: String? = null,
        order: String = "desc",
    ) = viewModelScope.launch {
        searchRepositorySafeCall(searchText, sort, order, pageSize, page)
    }

    private suspend fun searchRepositorySafeCall(
        searchText: String,
        sort: String? = null,
        order: String = "desc",
        pageSize: Int,
        page: Int,
    ) {
        _searchRepositoryResponse.postValue(Resource.Loading())
        _searchRepositoryResponse.postValue(globalSafeCall(getApplication()) {
            apiRepository.searchRepository(searchText, pageSize, page, sort, order)
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
}