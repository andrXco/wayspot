package com.example.wayspot.data.datasource

import com.example.wayspot.data.dto.PlaceDto

interface PlaceRemoteDataSource {

    suspend fun getAllPlaces(): List<PlaceDto>
    suspend fun getPlaceById(id: String): PlaceDto
}