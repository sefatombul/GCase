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
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    application: Application, private val authRepository: AuthRepository
) : AndroidViewModel(application) {

    private var _getAccessTokenResponse: MutableLiveData<Resource<AccessToken>?> = MutableLiveData()
    val getAccessTokenResponse: LiveData<Resource<AccessToken>?> get() = _getAccessTokenResponse

    private var _getAccessTokenWithRefreshTokenResponse: MutableLiveData<Resource<String>?> =
        MutableLiveData()
    val getAccessTokenWithRefreshTokenResponse: LiveData<Resource<String>?> get() = _getAccessTokenWithRefreshTokenResponse

    private var _getAuthTokenResponse: MutableLiveData<Resource<ResponseBody>?> = MutableLiveData()
    val getAuthTokenResponse: LiveData<Resource<ResponseBody>?> get() = _getAuthTokenResponse

    private var _setAuthTokenClearResponse: MutableLiveData<Resource<Any>?> = MutableLiveData()
    val setAuthTokenClearResponse: LiveData<Resource<Any>?> get() = _setAuthTokenClearResponse

    fun clearSetAuthTokenClearResponse() {
        _setAuthTokenClearResponse.postValue(null)
    }

    fun clearGetAuthTokenResponse() {
        _getAuthTokenResponse.postValue(null)
    }

    fun clearGetAccessTokenResponse() {
        _getAccessTokenResponse.postValue(null)
    }

    fun clearGetAccessTokenWithRefreshTokenResponse() {
        _getAccessTokenWithRefreshTokenResponse.postValue(null)
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


    fun getAccessTokenWithRefreshTokenResponse(refreshToken: String) = viewModelScope.launch {
        getAccessTokenWithRefreshTokenResponseSafeCall(refreshToken)
    }

    private suspend fun getAccessTokenWithRefreshTokenResponseSafeCall(refreshToken: String) {
        _getAccessTokenWithRefreshTokenResponse.postValue(Resource.Loading())
        _getAccessTokenWithRefreshTokenResponse.postValue(globalSafeCall(getApplication()) {
            authRepository.getAccessTokenWithRefreshToken(refreshToken)
        })
    }

    fun getAuthToken() = viewModelScope.launch {
        getAuthTokenSafeCall()
    }

    private suspend fun getAuthTokenSafeCall() {
        _getAuthTokenResponse.postValue(Resource.Loading())
        _getAuthTokenResponse.postValue(globalSafeCall(getApplication()) {
            authRepository.getAuthToken()
        })
    }

    fun setAuthTokenClear() = viewModelScope.launch {
        setAuthTokenClearSafeCall()
    }

    private suspend fun setAuthTokenClearSafeCall() {
        _setAuthTokenClearResponse.postValue(Resource.Loading())
        _setAuthTokenClearResponse.postValue(globalSafeCall(getApplication()) {
            authRepository.setAuthTokenClear()
        })
    }

}