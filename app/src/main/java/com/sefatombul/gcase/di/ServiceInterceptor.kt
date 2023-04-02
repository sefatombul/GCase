package com.sefatombul.gcase.di

import com.sefatombul.gcase.utils.Constants
import com.sefatombul.gcase.utils.PreferencesRepository
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServiceInterceptor @Inject constructor(): Interceptor {
    @Inject
    lateinit var preferencesRepository: PreferencesRepository
    override fun intercept(chain: Interceptor.Chain): Response {
        val original: Request = chain.request()
        val requestBuilder: Request.Builder = original.newBuilder()
        val token = preferencesRepository.getStringPreferences(Constants.ACCESS_TOKEN)
        if (!token.isNullOrBlank()){
            Timber.e("ACCESS_TOKEN $token")
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        val request: Request = requestBuilder.build()
        return chain.proceed(request)
    }
}