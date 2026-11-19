package com.example.wayspot.ui.screens.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.wayspot.R
import com.example.wayspot.ui.components.WayspotImage

/** Presenta las fotografías de una reseña en el feed usando el cargador Coil común de WaySpot. */
@Composable
fun HomeReviewPhotos(
    photoUrls: List<String>,
    placeTitle: String,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        itemsIndexed(
            items = photoUrls,
            key = { index, url -> "$index-$url" }
        ) { index, url ->
            WayspotImage(
                imageModel = url,
                contentDescription = stringResource(
                    R.string.home_review_photo_content_description,
                    index + 1,
                    placeTitle
                ),
                modifier = Modifier
                    .width(156.dp)
                    .height(160.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}
