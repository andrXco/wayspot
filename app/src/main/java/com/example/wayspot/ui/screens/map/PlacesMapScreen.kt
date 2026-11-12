package com.example.wayspot.ui.screens.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.wayspot.data.local.PreviewDataPopular
import com.example.wayspot.data.model.place.PlaceInfo
import com.example.wayspot.data.model.place.toPlaceInfo
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.screens.map.components.PlacesMapLayout
import com.example.wayspot.ui.theme.WayspotTheme

/** Adapta argumentos y eventos del mapa al ViewModel del destino. */
@Composable
fun PlacesMapScreen(
    viewModel: PlacesMapViewModel,
    focusedPlaceId: String?,
    onPlaceClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(focusedPlaceId) {
        viewModel.selectPlace(focusedPlaceId)
    }

    PlacesMapLayout(
        places = state.places,
        selectedPlace = state.selectedPlace,
        selectedPlaceId = state.selectedPlaceId,
        isLoading = state.isLoading,
        hasError = state.hasError,
        onPlaceSelected = viewModel::selectPlace,
        onPlaceClick = onPlaceClick,
        onRetry = viewModel::retry,
        modifier = modifier
    )
}

/** Orquesta las secciones a partir de estado inmutable y callbacks. */
@Composable
fun PlacesMapContent(
    places: List<PlaceInfo>,
    selectedPlace: PlaceInfo?,
    selectedPlaceId: String?,
    isLoading: Boolean,
    hasError: Boolean,
    onPlaceSelected: (String) -> Unit,
    onPlaceClick: (String) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    PlacesMapLayout(
        places = places,
        selectedPlace = selectedPlace,
        selectedPlaceId = selectedPlaceId,
        isLoading = isLoading,
        hasError = hasError,
        onPlaceSelected = onPlaceSelected,
        onPlaceClick = onPlaceClick,
        onRetry = onRetry,
        modifier = modifier
    )
}

@WayspotMultiPreview
@Composable
private fun PlacesMapScreenPreview() {
    val context = LocalContext.current
    val places = PreviewDataPopular.listPlaces.map { place ->
        place.toPlaceInfo(context::getString)
    }

    WayspotTheme {
        PlacesMapContent(
            places = places,
            selectedPlace = places.firstOrNull(),
            selectedPlaceId = places.firstOrNull()?.id,
            isLoading = false,
            hasError = false,
            onPlaceSelected = {},
            onPlaceClick = {},
            onRetry = {}
        )
    }
}
