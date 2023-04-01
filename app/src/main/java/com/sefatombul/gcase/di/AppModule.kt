package com.sefatombul.gcase.di

import android.content.Context
import android.content.SharedPreferences
import android.os.Environment
import com.sefatombul.gcase.data.remote.ApiService
import com.sefatombul.gcase.data.remote.auth.AuthService
import com.sefatombul.gcase.data.remote.auth.WoogletService
import com.sefatombul.gcase.utils.Constants
import com.sefatombul.gcase.utils.Constants.CACHE_MAX_SIZE
import com.sefatombul.gcase.utils.PreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE)

    @Singleton
    @Provides
    fun providePreferencesHelper(sharedPreferences: SharedPreferences): PreferencesRepository =
        PreferencesRepository(sharedPreferences)

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Singleton
    @Provides
    fun provideConvertorFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @AuthOkHttpClient
    @Provides
    fun provideHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient {
        val cache = Cache(Environment.getDownloadCacheDirectory(), CACHE_MAX_SIZE.toLong())
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .cache(cache)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @ApiOkHttpClient
    @Provides
    fun provideApiHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        serviceInterceptor: ServiceInterceptor
    ): OkHttpClient {
        val cache = Cache(Environment.getDownloadCacheDirectory(), CACHE_MAX_SIZE.toLong())
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .cache(cache)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(serviceInterceptor)
            .build()
    }

    @AuthRetrofit
    @Provides
    fun provideAuthRetrofit(
        @AuthOkHttpClient okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.GITHUB_AUTH_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @WoogletRetrofit
    @Provides
    fun provideWoogletRetrofit(
        @AuthOkHttpClient okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.WOOGLET_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @ApiRetrofit
    @Provides
    fun provideApiRetrofit(
        @ApiOkHttpClient okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.GITHUB_API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(gsonConverterFactory)
            .build()
    }


    @Singleton
    @Provides
    fun provideAuthService(@AuthRetrofit retrofit: Retrofit): AuthService {
        return retrofit.create(AuthService::class.java)
    }

    @Singleton
    @Provides
    fun provideWoogletService(@WoogletRetrofit retrofit: Retrofit): WoogletService {
        return retrofit.create(WoogletService::class.java)
    }

    @Singleton
    @Provides
    fun provideApiService(@ApiRetrofit retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }


}