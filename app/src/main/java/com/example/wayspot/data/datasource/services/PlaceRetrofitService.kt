package com.example.wayspot.data.datasource.services

import com.example.wayspot.data.dto.PlaceDto
import retrofit2.http.GET
import retrofit2.http.Path


interface PlaceRetrofitService {

    @GET("/places")
    suspend fun getPlaces(): List<PlaceDto>

    @GET("/places/{id}")
    suspend fun getPlaceById(
        @Path("id") id: String
    ): PlaceDto
}