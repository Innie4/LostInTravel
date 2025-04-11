package com.example.lostintravel.di

import com.example.lostintravel.data.remote.api.DestinationApi
import com.example.lostintravel.data.remote.interceptor.LoggingInterceptor
import com.example.lostintravel.data.remote.interceptor.NetworkConnectionInterceptor
import com.example.lostintravel.data.remote.util.ApiResponseHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    
    private const val BASE_URL = "https://lostapi.frontendlabs.co.uk/"
    
    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: LoggingInterceptor,
        networkConnectionInterceptor: NetworkConnectionInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(networkConnectionInterceptor)
            .addInterceptor(loggingInterceptor.create())
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }
    
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    @Provides
    @Singleton
    fun provideDestinationApi(retrofit: Retrofit): DestinationApi {
        return retrofit.create(DestinationApi::class.java)
    }
    
    @Provides
    @Singleton
    fun provideApiResponseHandler(): ApiResponseHandler {
        return ApiResponseHandler()
    }
}