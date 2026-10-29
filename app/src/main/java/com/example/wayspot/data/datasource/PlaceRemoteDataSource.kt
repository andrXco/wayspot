package com.example.wayspot.data.datasource

import com.example.wayspot.data.dto.PlaceDtoGeneric

interface PlaceRemoteDataSource {

    suspend fun getAllPlaces(): List<PlaceDtoGeneric>

    suspend fun getPlaceById(id: String): PlaceDtoGeneric
}
