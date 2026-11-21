package com.example.wayspot.ui.screens.reviewdetail.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.wayspot.R
import com.example.wayspot.ui.components.WayspotImage

@Composable
fun ReviewDetailPhotoGallery(
    photoUrls: List<String>,
    placeTitle: String,
    modifier: Modifier = Modifier
) {
    if (photoUrls.isEmpty()) return

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .height(152.dp)
    ) {
        itemsIndexed(
            items = photoUrls,
            key = { index, url -> "$index-$url" }
        ) { index, photoUrl ->
            WayspotImage(
                imageModel = photoUrl,
                contentDescription = stringResource(
                    R.string.home_review_photo_content_description,
                    index + 1,
                    placeTitle
                ),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillParentMaxWidth(if (photoUrls.size > 1) 0.5f else 1f)
                    .height(152.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
        }
    }
}
