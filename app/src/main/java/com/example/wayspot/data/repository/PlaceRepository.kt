package com.example.wayspot.data.repository

import com.example.wayspot.data.datasource.implementation.firestore.PlaceFirestoreDataSourceImplementation
import com.example.wayspot.data.model.place.Place
import com.example.wayspot.data.model.place.PlaceInfo
import javax.inject.Inject

class PlaceRepository @Inject constructor(
    private val placeRemoteDataSource: PlaceFirestoreDataSourceImplementation
) {
    suspend fun getPlaces(): Result<List<PlaceInfo>> {
        return try {
            val places = placeRemoteDataSource.getAllPlaces()

            val placesInfo = places.map {
                it.toPlaceInfo()
            }

            Result.success(placesInfo)
        } catch (e: Exception) {
            Result.failure(
                Exception("No se pudieron cargar los lugares")
            )
        }
    }

    suspend fun getExplorePlaces(): Result<List<Place>> {
        return try {
            Result.success(placeRemoteDataSource.getAllPlaces().map { it.toPlace() })
        } catch (e: Exception) {
            Result.failure(Exception("No se pudieron cargar los lugares"))
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
