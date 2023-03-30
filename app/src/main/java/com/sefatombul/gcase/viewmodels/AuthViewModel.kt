package com.sefatombul.gcase.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sefatombul.gcase.data.model.AccessToken
import com.sefatombul.gcase.data.repository.AuthRepository
import com.sefatombul.gcase.utils.Resource
import com.sefatombul.gcase.utils.globalSafeCall
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    application: Application, private val authRepository: AuthRepository
) : AndroidViewModel(application) {

    private var _getAccessTokenResponse: MutableLiveData<Resource<AccessToken>?> = MutableLiveData()
    val getAccessTokenResponse: LiveData<Resource<AccessToken>?> get() = _getAccessTokenResponse

    fun clearGetAccessTokenResponse() {
        _getAccessTokenResponse.postValue(null)
    }

    fun getAccessToken(code: String) = viewModelScope.launch {
        getAccessTokenSafeCall(code)
    }

    private suspend fun getAccessTokenSafeCall(code: String) {
        _getAccessTokenResponse.postValue(Resource.Loading())
        _getAccessTokenResponse.postValue(globalSafeCall(getApplication()) {
            authRepository.getAccessToken(code)
        })
    }

}