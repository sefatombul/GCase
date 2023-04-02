package com.sefatombul.gcase.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sefatombul.gcase.data.model.search.Items
import com.sefatombul.gcase.data.repository.ApiRepository
import com.sefatombul.gcase.utils.Resource
import com.sefatombul.gcase.utils.globalSafeCall
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepositoryViewModel @Inject constructor(
    application: Application, private val apiRepository: ApiRepository
) : AndroidViewModel(application) {

    private var _userRepositoryResponse: MutableLiveData<Resource<List<Items>>?> = MutableLiveData()
    val userRepositoryResponse: LiveData<Resource<List<Items>>?> get() = _userRepositoryResponse

    private var _getStarredRepositoriesResponse: MutableLiveData<Resource<List<Items>>?> =
        MutableLiveData()
    val getStarredRepositoriesResponse: LiveData<Resource<List<Items>>?> get() = _getStarredRepositoriesResponse

    fun clearGetStarredRepositoriesResponse() {
        _getStarredRepositoriesResponse.postValue(null)
    }


    fun clearUserRepositoryResponse() {
        _userRepositoryResponse.postValue(null)
    }

    fun userRepository() = viewModelScope.launch {
        userRepositorySafeCall()
    }

    private suspend fun userRepositorySafeCall() {
        _userRepositoryResponse.postValue(Resource.Loading())
        _userRepositoryResponse.postValue(globalSafeCall(getApplication()) {
            apiRepository.userRepository()
        })
    }

    fun getStarredRepositories(
        pageSize: Int,
        page: Int,
    ) = viewModelScope.launch {
        getStarredRepositoriesSafeCall(pageSize, page)
    }

    private suspend fun getStarredRepositoriesSafeCall(
        pageSize: Int,
        page: Int,
    ) {
        _getStarredRepositoriesResponse.postValue(Resource.Loading())
        _getStarredRepositoriesResponse.postValue(globalSafeCall(getApplication()) {
            apiRepository.getStarredRepositories(pageSize, page)
        })
    }

}