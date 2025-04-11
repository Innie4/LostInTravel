package com.example.lostintravel.data.remote.api

import com.example.lostintravel.data.remote.dto.DestinationDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DestinationApi {
    @GET("destinations")
    suspend fun getDestinations(): Response<List<DestinationDto>>
    
    @GET("destinations/{id}")
    suspend fun getDestinationById(@Path("id") id: String): Response<DestinationDto>
    
    @GET("destinations/search")
    suspend fun searchDestinations(@Query("query") query: String): Response<List<DestinationDto>>
    
    @GET("destinations/popular")
    suspend fun getPopularDestinations(): Response<List<DestinationDto>>
    
    @GET("destinations/recommended")
    suspend fun getRecommendedDestinations(): Response<List<DestinationDto>>
}