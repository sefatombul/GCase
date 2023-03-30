package com.sefatombul.gcase.data.repository

import com.sefatombul.gcase.data.remote.AuthService
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authService: AuthService
) {
    suspend fun getAccessToken(code: String) = authService.getAccessToken(code = code)
}