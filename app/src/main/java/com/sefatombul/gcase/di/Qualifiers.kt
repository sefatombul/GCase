package com.sefatombul.gcase.di

import javax.inject.Qualifier


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class WoogletRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ApiOkHttpClient
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ApiRetrofit