package com.sefatombul.gcase.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sefatombul.gcase.data.model.ProfileResponseModel
import com.sefatombul.gcase.data.model.search.Items
import com.sefatombul.gcase.data.remote.ApiService
import com.sefatombul.gcase.data.repository.ApiRepository
import com.sefatombul.gcase.utils.Resource
import com.sefatombul.gcase.utils.globalSafeCall
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    application: Application, private val apiRepository: ApiRepository
) : AndroidViewModel(application) {

    private var _getUserProfileResponse: MutableLiveData<Resource<ProfileResponseModel>?> =
        MutableLiveData()
    val getUserProfileResponse: LiveData<Resource<ProfileResponseModel>?> get() = _getUserProfileResponse

    fun clearGetUserProfileResponse() {
        _getUserProfileResponse.postValue(null)
    }

    fun getUserProfile() = viewModelScope.launch {
        getUserProfileSafeCall()
    }

    private suspend fun getUserProfileSafeCall() {
        _getUserProfileResponse.postValue(Resource.Loading())
        _getUserProfileResponse.postValue(globalSafeCall(getApplication()) {
            apiRepository.getUserProfile()
        })
    }

}