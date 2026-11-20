
package com.example.wayspot.data.dto

import com.example.wayspot.data.model.review.ReviewInfo
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import com.example.wayspot.data.model.place.PlaceCoordinates

/**
 * Contrato de `reviews` en Firestore. `createdAt` es un Timestamp del servidor;
 * queda nulo para documentos anteriores a ese campo y no aparecen en el mapa reciente.
 */
data class ReviewFirestoreDto(
    val id: String = "",
    val rating: Int = 0,
    val title: String? = null,
    val description: String = "",
    val userId: String = "",
    val placeId: String? = null,
    val coordinates: GeoPoint? = null,
    val reviewDate: String? = null,
    val createdAt: Timestamp? = null,
    val authorName: String? = null,
    val user: ReviewUserDto? = null,
    val likesCount: Int = 0,
    val liked: Boolean = false,
    val photoUrls: List<String> = emptyList()
) : ReviewDtoGeneric() {

    override fun toReviewInfo(): ReviewInfo {
        return ReviewInfo(
            id = id,
            rating = rating,
            title = title,
            description = description,
            userId = userId,
            placeId = placeId?.takeIf(String::isNotBlank),
            coordinates = coordinates?.let { point ->
                PlaceCoordinates(point.latitude, point.longitude)
            },
            reviewDate = reviewDate,
            authorName = user?.name ?: authorName,
            authorUsername = user?.username,
            authorAvatarUrl = user?.avatarUrl,
            likesCount = likesCount,
            liked = liked,
            createdAtMillis = createdAt?.toDate()?.time,
            photoUrls = photoUrls

        )
    }

}
