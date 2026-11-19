package com.example.wayspot.ui.screens.placedetail

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
import com.example.wayspot.R
import com.example.wayspot.data.local.PreviewData
import com.example.wayspot.data.local.PreviewDataPopular
import com.example.wayspot.data.model.place.toPlaceInfo
import com.example.wayspot.ui.components.map.MapRenderStatus
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.screens.placedetail.components.PlaceDetailBody
import com.example.wayspot.ui.screens.placedetail.components.PlaceDetailHero
import com.example.wayspot.ui.screens.placedetail.components.PlaceDetailLoadFeedback
import com.example.wayspot.ui.theme.WayspotTheme
import com.example.wayspot.ui.screens.placedetail.components.PlaceReviewCard
import com.example.wayspot.ui.screens.placedetail.components.RecentReviewsHeader

/** Sincroniza el argumento de ruta y conecta el estado y las acciones del detalle. */
@Composable
fun PlaceDetailScreen(
    placeDetailViewModel: PlaceDetailViewModel,
    placeId: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    onSaveClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
    onWriteReviewClick: () -> Unit = {},
    onReviewClick: (String) -> Unit = {},
    onMapClick: (String) -> Unit = {}
) {
    val state by placeDetailViewModel.uiState.collectAsState()

    LaunchedEffect(placeId) {
        placeDetailViewModel.loadPlace(placeId)
    }

    PlaceDetailContent(
        state = state,
        onBackClick = onBackClick,
        onSaveClick = onSaveClick,
        onShareClick = onShareClick,
        onWriteReviewClick = onWriteReviewClick,
        onReviewClick = onReviewClick,
        onMapClick = onMapClick,
        onRetryPlace = placeDetailViewModel::retryPlace,
        onRetryReviews = placeDetailViewModel::retryReviews,
        modifier = modifier,
        onMapRenderStatusChanged = placeDetailViewModel::onMapRenderStatusChanged,
        onRetryMapRender = placeDetailViewModel::retryMapRender
    )
}

/** Compone la portada y el cuerpo del detalle como secciones independientes de una lista vertical. */
@Composable
fun PlaceDetailContent(
    state: PlaceDetailState,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    onShareClick: () -> Unit,
    onWriteReviewClick: () -> Unit,
    onReviewClick: (String) -> Unit,
    onMapClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    onRetryPlace: () -> Unit = {},
    onRetryReviews: () -> Unit = {},
    onMapRenderStatusChanged: (MapRenderStatus) -> Unit = {},
    onRetryMapRender: () -> Unit = {}
) {
    val place = state.place
    if (place == null) {
        val failed = state.placeStatus == PlaceDetailLoadStatus.Failed
        PlaceDetailLoadFeedback(
            messageRes = if (failed) R.string.place_detail_unavailable else R.string.place_detail_loading,
            onRetry = onRetryPlace.takeIf { failed },
            onBack = onBackClick,
            modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface)
        )
        return
    }

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
                place = if (state.reviewsStatus == PlaceDetailLoadStatus.Ready) {
                    place.copy(reviewCount = state.reviews.size)
                } else {
                    place
                },
                onShareClick = onShareClick,
                onWriteReviewClick = onWriteReviewClick,
                onMapClick = { onMapClick(place.id) },
                modifier = Modifier.fillMaxWidth(),
                mapRenderState = state.mapRenderState,
                onMapRenderStatusChanged = onMapRenderStatusChanged,
                onRetryMapRender = onRetryMapRender
            )
        }
        if (state.reviewsStatus != PlaceDetailLoadStatus.Ready) {
            item(key = "reviews-status") {
                val failed = state.reviewsStatus == PlaceDetailLoadStatus.Failed
                PlaceDetailLoadFeedback(
                    messageRes = if (failed) {
                        R.string.place_detail_reviews_unavailable
                    } else {
                        R.string.place_detail_reviews_loading
                    },
                    onRetry = onRetryReviews.takeIf { failed },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        } else if (state.reviews.isNotEmpty()) {
            item(key = "reviews-header") {
                RecentReviewsHeader(
                    reviewCount = state.reviews.size,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 20.dp, bottom = 8.dp)
                )
            }
            items(state.reviews, key = { it.id }) { review ->
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
            state = PlaceDetailState(
                place = PreviewDataPopular.cerroMonserrate.toPlaceInfo(
                    LocalContext.current::getString
                ),
                placeStatus = PlaceDetailLoadStatus.Ready,
                reviews = listOf(PreviewData.placeDetailReview),
                reviewsStatus = PlaceDetailLoadStatus.Ready
            ),
            onBackClick = {},
            onSaveClick = {},
            onShareClick = {},
            onWriteReviewClick = {},
            onMapClick = {},
            onReviewClick = {}
        )
    }
}
