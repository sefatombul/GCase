package com.sefatombul.gcase.data.repository

import com.sefatombul.gcase.data.remote.ApiService
import javax.inject.Inject

class ApiRepository @Inject constructor(
    private val apiService: ApiService
) {

}