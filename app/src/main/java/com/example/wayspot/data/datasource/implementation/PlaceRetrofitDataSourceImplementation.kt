package com.example.wayspot.data.datasource.implementation

import com.example.wayspot.data.datasource.PlaceRemoteDataSource
import com.example.wayspot.data.datasource.services.PlaceRetrofitService
import com.example.wayspot.data.dto.PlaceDto
import javax.inject.Inject

class PlaceRetrofitDataSourceImplementation @Inject constructor(
    private val service: PlaceRetrofitService
) : PlaceRemoteDataSource {

    override suspend fun getAllPlaces(): List<PlaceDto> {
        return service.getPlaces()
    }

    override suspend fun getPlaceById(id: String): PlaceDto {
        return service.getPlaceById(id)
    }
}