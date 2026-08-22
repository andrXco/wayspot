package com.example.wayspot.ui.screens.placedetail

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.wayspot.data.model.Places
import com.example.wayspot.data.local.PreviewDataPopular
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.screens.placedetail.components.PlaceDetailBody
import com.example.wayspot.ui.screens.placedetail.components.PlaceDetailHero
import com.example.wayspot.ui.theme.WayspotTheme

@Composable
fun PlaceDetailScreen(
    place: Places,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    onSaveClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
    onWriteReviewClick: () -> Unit = {},
    onSeeAllReviewsClick: () -> Unit = {}
) {
    PlaceDetailContent(
        place = place,
        onBackClick = onBackClick,
        onSaveClick = onSaveClick,
        onShareClick = onShareClick,
        onWriteReviewClick = onWriteReviewClick,
        onSeeAllReviewsClick = onSeeAllReviewsClick,
        modifier = modifier
    )
}

@Composable
fun PlaceDetailContent(
    place: Places,
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
        PlaceDetailScreen(
            place = PreviewDataPopular.samplePlaces1,
            onBackClick = {}
        )
    }
}
