
package com.example.wayspot.data.datasource.implementation.firestore

import com.example.wayspot.data.dto.PlaceFirestoreDto
import com.example.wayspot.data.datasource.PlaceRemoteDataSource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
class PlaceFirestoreDataSourceImplementation @Inject constructor(
    private val db: FirebaseFirestore
) : PlaceRemoteDataSource {


    override suspend fun getAllPlaces(): List<PlaceFirestoreDto> {
        val snapshot = db.collection("places").get().await()

        val places = mutableListOf<PlaceFirestoreDto>()

        for (document in snapshot.documents) {
            val place = document.toObject(PlaceFirestoreDto::class.java)

            if (place != null) {
                places.add(place.copy(id = document.id))
            }
        }

        return places
    }

    override suspend fun getPlaceById(id: String): PlaceFirestoreDto {
        val docRef = db.collection("places").document(id)

        val snapshot = docRef.get().await()

        val place = snapshot.toObject(PlaceFirestoreDto::class.java)
            ?: throw Exception("No se pudo obtener el lugar")

        return place.copy(id = snapshot.id)
    }

}
