package com.sefatombul.gcase.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sefatombul.gcase.data.local.RecentSearchModel
import com.sefatombul.gcase.data.repository.RecentSearchRepository
import com.sefatombul.gcase.utils.Resource
import com.sefatombul.gcase.utils.globalSafeCallDB
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecentSearchViewModel @Inject constructor(
    application: Application,
    private val recentSearchRepository: RecentSearchRepository
) : AndroidViewModel(application) {
    private val _insertRecentSearchResponse: MutableLiveData<Resource<Long>?> =
        MutableLiveData()
    val insertRecentSearchResponse: LiveData<Resource<Long>?> get() = _insertRecentSearchResponse

    private val _deleteRecentSearchWithWordTextResponse: MutableLiveData<Resource<Int>?> =
        MutableLiveData()
    val deleteRecentSearchWithWordTextResponse: LiveData<Resource<Int>?> get() = _deleteRecentSearchWithWordTextResponse

    private val _getRecentSearchLocalResponse: MutableLiveData<Resource<List<RecentSearchModel>>?> =
        MutableLiveData()
    val getRecentSearchLocalResponse: LiveData<Resource<List<RecentSearchModel>>?> get() = _getRecentSearchLocalResponse


    private val _deleteAllTextResponse: MutableLiveData<Resource<Int>?> =
        MutableLiveData()
    val deleteAllTextResponse: LiveData<Resource<Int>?> get() = _deleteAllTextResponse

    fun clearDeleteAllTextResponseResponse() {
        _deleteAllTextResponse.postValue(null)
    }

    fun clearGetRecentSearchLocalResponse() {
        _getRecentSearchLocalResponse.postValue(null)
    }

    fun clearInsertRecentSearchResponse() {
        _insertRecentSearchResponse.postValue(null)
    }

    fun clearDeleteRecentSearchWithWordTextResponse() {
        _deleteRecentSearchWithWordTextResponse.postValue(null)
    }

    fun deleteRecentSearchWithWordText(word: String, type: String) = viewModelScope.launch {
        deleteRecentSearchWithWordTextSafeCall(word, type)
    }

    private suspend fun deleteRecentSearchWithWordTextSafeCall(word: String, type: String) {
        _deleteRecentSearchWithWordTextResponse.postValue(Resource.Loading())
        _deleteRecentSearchWithWordTextResponse.postValue(globalSafeCallDB(getApplication()) {
            recentSearchRepository.deleteRecentSearchWithWordText(word, type)
        })
    }

    fun insertRecentSearch(recentSearchModel: RecentSearchModel) = viewModelScope.launch {
        insertRecentSearchSafeCall(recentSearchModel)
    }

    private suspend fun insertRecentSearchSafeCall(recentSearchModel: RecentSearchModel) {
        _insertRecentSearchResponse.postValue(Resource.Loading())
        _insertRecentSearchResponse.postValue(globalSafeCallDB(getApplication()) {
            recentSearchRepository.insertRecentSearch(recentSearchModel)
        })
    }

    fun getRecentSearchLocal(limit: Int, type: String) = viewModelScope.launch {
        getRecentSearchLocalSafeCall(limit, type)
    }

    private suspend fun getRecentSearchLocalSafeCall(limit: Int, type: String) {
        _getRecentSearchLocalResponse.postValue(Resource.Loading())
        _getRecentSearchLocalResponse.postValue(globalSafeCallDB(getApplication()) {
            recentSearchRepository.getRecentSearchLocal(limit, type)
        })
    }

    fun deleteAllText(type: String) = viewModelScope.launch {
        deleteAllTextSafeCall(type)
    }

    private suspend fun deleteAllTextSafeCall(type: String) {
        _deleteAllTextResponse.postValue(Resource.Loading())
        _deleteAllTextResponse.postValue(globalSafeCallDB(getApplication()) {
            recentSearchRepository.deleteAllText(type)
        })
    }

}