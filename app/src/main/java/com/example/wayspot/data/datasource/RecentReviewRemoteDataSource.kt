package com.example.wayspot.data.datasource

import com.example.wayspot.data.dto.ReviewDtoGeneric
import kotlinx.coroutines.flow.Flow

/** Observa reseñas desde un corte fijo en milisegundos UTC, sin exponer tipos de Firestore. */
interface RecentReviewRemoteDataSource {
    fun observeReviewsSince(cutoffMillis: Long): Flow<List<ReviewDtoGeneric>>
}
