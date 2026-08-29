package com.example.wayspot.ui.screens.newreview.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.wayspot.R
import com.example.wayspot.data.model.ReviewRules
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.theme.WayspotTheme

@Composable
fun ReviewPhotoSection(
    photoUris: List<String>,
    onAddPhotosClick: () -> Unit,
    onRemovePhotoClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    maxPhotos: Int = ReviewRules.MAX_PHOTOS
) {
    val safeMaxPhotos = maxPhotos.coerceAtLeast(0)

    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.new_review_add_photos_optional),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.new_review_photos_limit, safeMaxPhotos),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelSmall
        )
        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(
                items = photoUris,
                key = { _, photoUri -> photoUri }
            ) { index, photoUri ->
                SelectedPhotoItem(
                    photoUri = photoUri,
                    photoIndex = index,
                    onRemovePhotoClick = onRemovePhotoClick
                )
            }

            if (photoUris.size < safeMaxPhotos) {
                item(key = "new_review_add_photo") {
                    AddPhotoCard(onClick = onAddPhotosClick)
                }
            }
        }
    }
}

@Composable
private fun AddPhotoCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.size(88.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.20f),
            contentColor = MaterialTheme.colorScheme.primary
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.42f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Filled.AddAPhoto,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.new_review_add_photo),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun SelectedPhotoItem(
    photoUri: String,
    photoIndex: Int,
    onRemovePhotoClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(88.dp)
            .clip(RoundedCornerShape(12.dp))
    ) {
        AsyncImage(
            model = photoUri,
            contentDescription = stringResource(
                R.string.new_review_selected_photo_content_description,
                photoIndex + 1
            ),
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop
        )

        IconButton(
            onClick = { onRemovePhotoClick(photoUri) },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(48.dp)
                .padding(6.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.90f),
                    shape = CircleShape
                )
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = stringResource(
                    R.string.new_review_remove_photo_content_description,
                    photoIndex + 1
                ),
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@WayspotMultiPreview
@Composable
private fun ReviewPhotoSectionPreview() {
    WayspotTheme {
        ReviewPhotoSection(
            photoUris = emptyList(),
            onAddPhotosClick = {},
            onRemovePhotoClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
