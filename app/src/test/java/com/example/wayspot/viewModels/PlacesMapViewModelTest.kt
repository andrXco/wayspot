package com.example.wayspot.viewModels

import com.example.wayspot.data.model.place.NearbyPlacesResponseException
import com.example.wayspot.data.model.place.PlaceCoordinates
import com.example.wayspot.data.model.place.PlaceInfo
import com.example.wayspot.data.repository.NearbyPlacesRepository
import com.example.wayspot.data.repository.PlaceRepository
import com.example.wayspot.data.repository.ReviewRepository
import com.example.wayspot.ui.components.map.MapRenderStatus
import com.example.wayspot.ui.screens.map.MapCatalogStatus
import com.example.wayspot.ui.screens.map.MapNearbyStatus
import com.example.wayspot.ui.screens.map.PlacesMapViewModel
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PlacesMapViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private val placeRepository = mockk<PlaceRepository>()
    private val nearbyPlacesRepository = mockk<NearbyPlacesRepository>()
    private val reviewRepository = mockk<ReviewRepository>()
    private val center = PlaceCoordinates(4.71, -74.07)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        coEvery { placeRepository.getPlaces() } returns Result.success(emptyList())
        every { reviewRepository.getRecentReviews(any()) } returns flowOf(emptyList())
    }

    // Distingue el fallo de lugares del fallo de reseñas y permite recuperar el catálogo.
    @Test
    fun retry_whenPlacesFailed_loadsCatalogAgain() = runTest(testDispatcher) {
        coEvery { placeRepository.getPlaces() } returns Result.failure(IllegalStateException())
        val viewModel = PlacesMapViewModel(placeRepository, nearbyPlacesRepository, reviewRepository)
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.catalogStatus).isEqualTo(MapCatalogStatus.PlacesUnavailable)

        coEvery { placeRepository.getPlaces() } returns Result.success(emptyList())
        viewModel.retry()
        assertThat(viewModel.uiState.value.catalogStatus).isEqualTo(MapCatalogStatus.Loading)
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.catalogStatus).isEqualTo(MapCatalogStatus.Ready)
        coVerify(exactly = 2) { placeRepository.getPlaces() }
    }

    // El error de reseñas sigue disponible al salir de una búsqueda cercana fallida.
    @Test
    fun clearNearbySearch_preservesCatalogError() = runTest(testDispatcher) {
        every { reviewRepository.getRecentReviews(any()) } returns flow {
            throw IllegalStateException()
        }
        coEvery { nearbyPlacesRepository.findNearbyPlaces(any(), any(), any()) } returns
            Result.failure(IllegalStateException())
        val viewModel = PlacesMapViewModel(placeRepository, nearbyPlacesRepository, reviewRepository)
        advanceUntilIdle()

        viewModel.searchNearby(center)
        advanceUntilIdle()
        assertThat(viewModel.uiState.value.nearbyStatus).isEqualTo(MapNearbyStatus.Unavailable)

        viewModel.clearNearbySearch()

        assertThat(viewModel.uiState.value.nearbyStatus).isEqualTo(MapNearbyStatus.Idle)
        assertThat(viewModel.uiState.value.catalogStatus).isEqualTo(MapCatalogStatus.ReviewsUnavailable)
    }

    // Reintentar el mapa base no repite ni borra una consulta de lugares fallida.
    @Test
    fun retryMapRender_startsNewRenderAttemptIndependentlyOfCatalog() = runTest(testDispatcher) {
        coEvery { placeRepository.getPlaces() } returns Result.failure(IllegalStateException())
        val viewModel = PlacesMapViewModel(placeRepository, nearbyPlacesRepository, reviewRepository)
        advanceUntilIdle()

        viewModel.onMapRenderStatusChanged(MapRenderStatus.Failed)
        viewModel.retryMapRender()
        viewModel.retryMapRender()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.mapRenderState.status).isEqualTo(MapRenderStatus.Loading)
        assertThat(state.mapRenderState.attempt).isEqualTo(1)
        assertThat(state.catalogStatus).isEqualTo(MapCatalogStatus.PlacesUnavailable)
        coVerify(exactly = 1) { placeRepository.getPlaces() }
    }

    // Abre y conserva el lugar solicitado por la ruta aunque no tenga reseñas recientes.
    @Test
    fun focusPlace_beforeCatalogLoads_includesPlaceWithoutReviewsAfterRefresh() = runTest(testDispatcher) {
        val place = PlaceInfo(
            id = "requested-place",
            title = "Lugar solicitado",
            category = "",
            location = "",
            rating = 0.0,
            imageUrl = null,
            description = "",
            coordinates = center
        )
        coEvery { placeRepository.getPlaces() } returns Result.success(listOf(place))
        val viewModel = PlacesMapViewModel(placeRepository, nearbyPlacesRepository, reviewRepository)
        viewModel.focusPlace(place.id)
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.places).containsExactly(place)
        assertThat(viewModel.uiState.value.selectedPlace).isEqualTo(place)

        viewModel.refreshOnResume()
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.selectedPlace).isEqualTo(place)
    }

    // Una respuesta inválida termina en error, mientras que una lista vacía termina correctamente.
    @Test
    fun searchNearby_distinguishesInvalidResponseFromEmptyResults() = runTest(testDispatcher) {
        coEvery { nearbyPlacesRepository.findNearbyPlaces(any(), any(), any()) } returns
            Result.failure(NearbyPlacesResponseException("Respuesta inválida"))
        val viewModel = PlacesMapViewModel(placeRepository, nearbyPlacesRepository, reviewRepository)
        advanceUntilIdle()

        viewModel.searchNearby(center)
        assertThat(viewModel.uiState.value.nearbyStatus).isEqualTo(MapNearbyStatus.Searching)
        advanceUntilIdle()
        assertThat(viewModel.uiState.value.nearbyStatus).isEqualTo(MapNearbyStatus.InvalidResponse)

        coEvery { nearbyPlacesRepository.findNearbyPlaces(any(), any(), any()) } returns
            Result.success(emptyList())
        viewModel.retry()
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.nearbyStatus).isEqualTo(MapNearbyStatus.Ready)
        assertThat(viewModel.uiState.value.places).isEmpty()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
