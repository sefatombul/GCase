package com.sefatombul.gcase.data.repository

import com.sefatombul.gcase.data.model.RevokeAccessRequestModel
import com.sefatombul.gcase.data.remote.auth.AuthService
import com.sefatombul.gcase.data.remote.auth.WoogletService
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authService: AuthService,
    private val woogletService: WoogletService
) {
    suspend fun getAuthToken() = woogletService.getAuthToken()
    suspend fun setAuthTokenClear() = woogletService.setAuthTokenClear()
    suspend fun getAccessToken(code: String) = authService.getAccessToken(code = code)
    suspend fun revokeAccess(requestModel: RevokeAccessRequestModel) =
        authService.revokeAccess(requestModel = requestModel)

    suspend fun getAccessTokenWithRefreshToken(refreshToken: String) =
        authService.getAccessTokenWithRefreshToken(refreshToken = refreshToken)


}