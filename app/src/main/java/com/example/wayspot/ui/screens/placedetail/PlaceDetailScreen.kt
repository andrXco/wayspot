package com.example.wayspot.ui.screens.placedetail

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.wayspot.model.Places
import com.example.wayspot.ui.preview.PreviewDataPopular
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
    Scaffold(modifier = modifier.fillMaxSize()) { contentPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
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
