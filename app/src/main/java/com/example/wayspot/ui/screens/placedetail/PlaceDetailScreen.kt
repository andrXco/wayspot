package com.example.wayspot.ui.screens.placedetail

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.wayspot.data.model.PlaceInfo
import com.example.wayspot.data.model.ReviewInfo
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.screens.placedetail.components.PlaceDetailBody
import com.example.wayspot.ui.screens.placedetail.components.PlaceDetailHero
import com.example.wayspot.ui.theme.WayspotTheme

/** Sincroniza el argumento de ruta con el ViewModel y muestra el contenido solo cuando hay lugar. */
@Composable
fun PlaceDetailScreen(
    placeDetailViewModel: PlaceDetailViewModel,
    placeId: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    onSaveClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
    onWriteReviewClick: () -> Unit = {},
    onSeeAllReviewsClick: () -> Unit = {}
) {
    val state by placeDetailViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        placeDetailViewModel.loadPlace(placeId)
    }

    val place = state.place

    if (place != null) {
        PlaceDetailContent(
            place = place,
            reviews = state.reviews,
            onBackClick = onBackClick,
            onSaveClick = onSaveClick,
            onShareClick = onShareClick,
            onWriteReviewClick = onWriteReviewClick,
            onSeeAllReviewsClick = onSeeAllReviewsClick,
            modifier = modifier
        )
    }
}

/** Compone la portada y el cuerpo del detalle como secciones independientes de una lista vertical. */
@Composable
fun PlaceDetailContent(
    place: PlaceInfo,
    reviews: List<ReviewInfo>,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    onShareClick: () -> Unit,
    onWriteReviewClick: () -> Unit,
    onSeeAllReviewsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        item {
            PlaceDetailHero(
                place = place,
                onBackClick = onBackClick,
                onSaveClick = onSaveClick,
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            PlaceDetailBody(
                place = place,
                reviews = reviews,
                onShareClick = onShareClick,
                onWriteReviewClick = onWriteReviewClick,
                onSeeAllReviewsClick = onSeeAllReviewsClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@WayspotMultiPreview
@Composable
private fun PlaceDetailScreenPreview() {
    WayspotTheme {
        PlaceDetailContent(
            place = PlaceInfo(
                id = "1",
                title = "Monserrate",
                category = "Turismo",
                location = "Bogotá, Colombia",
                rating = 4.7,
                imageUrl = null,
                description = "Lugar turístico ubicado en Bogotá"
            ),
            reviews = emptyList(),
            onBackClick = {},
            onSaveClick = {},
            onShareClick = {},
            onWriteReviewClick = {},
            onSeeAllReviewsClick = {}
        )
    }
}