package com.example.wayspot.data.repository

import com.example.wayspot.data.datasource.PlaceRemoteDataSource
import com.example.wayspot.data.model.place.Place
import com.example.wayspot.data.model.place.PlaceInfo
import javax.inject.Inject
import kotlinx.coroutines.CancellationException

class PlaceRepository @Inject constructor(
    private val placeRemoteDataSource: PlaceRemoteDataSource
) {
    suspend fun getPlaces(): Result<List<PlaceInfo>> {
        return try {
            val places = placeRemoteDataSource.getAllPlaces()

            val placesInfo = places.map {
                it.toPlaceInfo()
            }

            Result.success(placesInfo)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getExplorePlaces(): Result<List<Place>> {
        return try {
            Result.success(placeRemoteDataSource.getAllPlaces().map { it.toPlace() })
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getPlaceById(id: String): Result<PlaceInfo> {
        return try {
            val place = placeRemoteDataSource.getPlaceById(id)

            Result.success(place.toPlaceInfo())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
