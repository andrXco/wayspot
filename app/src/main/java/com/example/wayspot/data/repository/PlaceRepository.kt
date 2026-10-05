package com.example.wayspot.data.repository

import com.example.wayspot.data.datasource.implementation.PlaceRetrofitDataSourceImplementation
import com.example.wayspot.data.dto.toPlaceInfo
import com.example.wayspot.data.model.PlaceInfo
import javax.inject.Inject

class PlaceRepository @Inject constructor(
    private val placeRemoteDataSource: PlaceRetrofitDataSourceImplementation
) {

    suspend fun getPlaces(): Result<List<PlaceInfo>> {
        return try {
            val places = placeRemoteDataSource.getAllPlaces()

            val placesInfo = places.map {
                it.toPlaceInfo()
            }

            Result.success(placesInfo)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getPlaceById(id: String): Result<PlaceInfo> {
        return try {
            val place = placeRemoteDataSource.getPlaceById(id)

            Result.success(place.toPlaceInfo())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}