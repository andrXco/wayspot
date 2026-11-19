package com.example.wayspot.viewModels

import com.example.wayspot.data.model.place.PlaceInfo
import com.example.wayspot.data.repository.PlaceRepository
import com.example.wayspot.data.repository.ReviewRepository
import com.example.wayspot.ui.screens.placedetail.PlaceDetailLoadStatus
import com.example.wayspot.ui.screens.placedetail.PlaceDetailViewModel
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PlaceDetailViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    private val placeRepository = mockk<PlaceRepository>()
    private val reviewRepository = mockk<ReviewRepository>()
    private val viewModel = PlaceDetailViewModel(placeRepository, reviewRepository)
    private val place = PlaceInfo(
        id = "lugar-1",
        title = "Lugar",
        category = "",
        location = "",
        rating = 0.0,
        imageUrl = null,
        description = ""
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        coEvery { placeRepository.getPlaceById(place.id) } returns Result.success(place)
        coEvery { reviewRepository.getReviewsByPlaceId(place.id) } returns Result.success(emptyList())
    }

    // El fallo de lugar produce un estado visible y se recupera reintentando esa consulta.
    @Test
    fun retryPlace_recoversAfterFailure() = runTest(dispatcher) {
        coEvery { placeRepository.getPlaceById(place.id) } returns Result.failure(IllegalStateException())
        viewModel.loadPlace(place.id)
        assertThat(viewModel.uiState.value.placeStatus).isEqualTo(PlaceDetailLoadStatus.Loading)
        advanceUntilIdle()
        assertThat(viewModel.uiState.value.placeStatus).isEqualTo(PlaceDetailLoadStatus.Failed)

        coEvery { placeRepository.getPlaceById(place.id) } returns Result.success(place)
        viewModel.retryPlace()
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.placeStatus).isEqualTo(PlaceDetailLoadStatus.Ready)
        assertThat(viewModel.uiState.value.place).isEqualTo(place)
        coVerify(exactly = 1) { reviewRepository.getReviewsByPlaceId(place.id) }
    }

    // Un error de reseñas permite seguir viendo el lugar y reintentar solo las reseñas.
    @Test
    fun retryReviews_keepsLoadedPlaceAvailable() = runTest(dispatcher) {
        coEvery { reviewRepository.getReviewsByPlaceId(place.id) } returns
            Result.failure(IllegalStateException())
        viewModel.loadPlace(place.id)
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.reviewsStatus).isEqualTo(PlaceDetailLoadStatus.Failed)
        assertThat(viewModel.uiState.value.place).isEqualTo(place)

        coEvery { reviewRepository.getReviewsByPlaceId(place.id) } returns Result.success(emptyList())
        viewModel.retryReviews()
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.reviewsStatus).isEqualTo(PlaceDetailLoadStatus.Ready)
        coVerify(exactly = 1) { placeRepository.getPlaceById(place.id) }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
