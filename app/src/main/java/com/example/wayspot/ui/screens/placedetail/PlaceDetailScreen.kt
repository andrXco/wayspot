package com.example.wayspot.ui.screens.placedetail

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.dp
import com.example.wayspot.data.local.PreviewData
import com.example.wayspot.data.local.PreviewDataPopular
import com.example.wayspot.data.model.place.PlaceInfo
import com.example.wayspot.data.model.review.ReviewInfo
import com.example.wayspot.data.model.place.toPlaceInfo
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.screens.placedetail.components.PlaceDetailBody
import com.example.wayspot.ui.screens.placedetail.components.PlaceDetailHero
import com.example.wayspot.ui.theme.WayspotTheme
import com.example.wayspot.ui.screens.placedetail.components.PlaceReviewCard
import com.example.wayspot.ui.screens.placedetail.components.RecentReviewsHeader

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
    onReviewClick: (String) -> Unit = {}
) {
    val state by placeDetailViewModel.uiState.collectAsState()

    LaunchedEffect(placeId) {
        placeDetailViewModel.loadPlace(placeId)
    }

    val context = LocalContext.current
    val place = state.place ?: state.fallbackPlace?.toPlaceInfo(
        context::getString
    )

    if (place != null) {
        PlaceDetailContent(
            place = place,
            reviews = state.reviews,
            onBackClick = onBackClick,
            onSaveClick = onSaveClick,
            onShareClick = onShareClick,
            onWriteReviewClick = onWriteReviewClick,
            onReviewClick = onReviewClick,
            onMapClick = {
                openPlaceInMaps(
                    context = context,
                    placeName = place.title,
                    location = place.location
                )
            },
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
    onReviewClick: (String) -> Unit,
    onMapClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
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
                place = place.copy(reviewCount = reviews.size),
                onShareClick = onShareClick,
                onWriteReviewClick = onWriteReviewClick,
                onMapClick = onMapClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (reviews.isNotEmpty()) {
            item(key = "reviews-header") {
                RecentReviewsHeader(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 20.dp, bottom = 8.dp)
                )
            }
            items(reviews, key = { it.id }) { review ->
                PlaceReviewCard(
                    review = review,
                    onClick = { onReviewClick(review.id) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@WayspotMultiPreview
@Composable
private fun PlaceDetailScreenPreview() {
    WayspotTheme {
        PlaceDetailContent(
            place = PreviewDataPopular.cerroMonserrate.toPlaceInfo(
                LocalContext.current::getString
            ),
            reviews = listOf(PreviewData.reviewDetailReview),
            onBackClick = {},
            onSaveClick = {},
            onShareClick = {},
            onWriteReviewClick = {},
            onMapClick = {},
            onReviewClick = {}
        )
    }
}

private fun openPlaceInMaps(
    context: Context,
    placeName: String,
    location: String
) {
    val query = Uri.encode("$placeName, $location")
    val mapsIntent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("geo:0,0?q=$query")
    )

    try {
        context.startActivity(mapsIntent)
    } catch (_: ActivityNotFoundException) {
        val browserIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://www.google.com/maps/search/?api=1&query=$query")
        )
        context.startActivity(browserIntent)
    }
}
