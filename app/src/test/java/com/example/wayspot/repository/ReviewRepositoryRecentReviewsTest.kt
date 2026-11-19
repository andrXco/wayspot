package com.example.wayspot.repository

import com.example.wayspot.data.datasource.RecentReviewRemoteDataSource
import com.example.wayspot.data.datasource.implementation.firestore.ReviewFirestoreDataSourceImplementation
import com.example.wayspot.data.dto.ReviewFirestoreDto
import com.example.wayspot.data.model.review.RecentReviewWindow
import com.example.wayspot.data.repository.AuthRepository
import com.example.wayspot.data.repository.ReviewRepository
import com.example.wayspot.data.repository.UserRepository
import com.google.common.truth.Truth.assertThat
import com.google.firebase.Timestamp
import io.mockk.every
import io.mockk.mockk
import java.util.Date
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ReviewRepositoryRecentReviewsTest {
    // El contrato entrega solo reviews con fecha válida y vuelve a calcular el tiempo en cada emisión.
    @Test
    fun getRecentReviews_filtersEachEmissionAndMapsFirestoreTimestamp() = runTest {
        val initialNow = 1_700_000_000_000L
        val cutoff = RecentReviewWindow.cutoffMillis(initialNow)
        var currentTime = initialNow
        val dataSource = mockk<RecentReviewRemoteDataSource>()
        every { dataSource.observeReviewsSince(cutoff) } returns flow {
            emit(listOf(review("recent", cutoff + 1L), review("future", initialNow + 1L), review("undated", null)))
            currentTime = cutoff + 1L + RecentReviewWindow.DURATION_MILLIS
            emit(listOf(review("recent", cutoff + 1L)))
        }
        val repository = ReviewRepository(
            reviewFirestoreDataSource = mockk<ReviewFirestoreDataSourceImplementation>(),
            authRepository = mockk<AuthRepository>(),
            userRepository = mockk<UserRepository>(),
            recentReviewRemoteDataSource = dataSource
        )

        val emissions = repository.getRecentReviews { currentTime }.toList()

        assertThat(emissions).hasSize(2)
        assertThat(emissions[0].map { it.id }).containsExactly("recent")
        assertThat(emissions[0].single().createdAtMillis).isEqualTo(cutoff + 1L)
        assertThat(emissions[1]).isEmpty()
    }

    private fun review(id: String, createdAtMillis: Long?): ReviewFirestoreDto =
        ReviewFirestoreDto(
            id = id,
            placeId = "place_1",
            createdAt = createdAtMillis?.let { Timestamp(Date(it)) }
        )
}
