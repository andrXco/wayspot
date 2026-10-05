package com.example.wayspot.ui.screens.placedetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.wayspot.R
import com.example.wayspot.data.model.PlaceInfo
import com.example.wayspot.ui.theme.EstrellaAmarilla
import com.example.wayspot.ui.theme.WayspotTheme

/** Agrupa ubicación, valoración y la acción de compartir del lugar. */
@Composable
internal fun PlaceMetadata(
    place: PlaceInfo,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PlaceLocationAndRating(
            place = place
        )

        SharePlaceButton(
            onClick = onShareClick
        )
    }
}

@Composable
private fun PlaceLocationAndRating(
    place: PlaceInfo,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        PlaceLocation(
            location = place.location
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        PlaceRating(
            rating = place.rating
        )
    }
}

@Composable
private fun PlaceLocation(
    location: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(18.dp)
        )

        Spacer(
            modifier = Modifier.width(4.dp)
        )

        Text(
            text = location,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun PlaceRating(
    rating: Double,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RatingStars(
            rating = rating.toInt(),
            size = 16.dp
        )

        Spacer(
            modifier = Modifier.width(8.dp)
        )

        Text(
            text = stringResource(
                R.string.rating_value,
                rating
            ),
            fontWeight = FontWeight.Bold
        )
    }
}

/** Dibuja una valoración discreta con el tamaño solicitado por la sección consumidora. */
@Composable
internal fun RatingStars(
    rating: Int,
    size: Dp,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
    ) {
        repeat(5) { starIndex ->
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = if (starIndex < rating) {
                    EstrellaAmarilla
                } else {
                    MaterialTheme.colorScheme.outlineVariant
                },
                modifier = Modifier.size(size)
            )
        }
    }
}

@Composable
private fun SharePlaceButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(40.dp)
            .background(
                MaterialTheme.colorScheme.surfaceVariant,
                RoundedCornerShape(12.dp)
            )
    ) {
        Icon(
            imageVector = Icons.Default.Share,
            contentDescription = stringResource(
                R.string.share_content_description
            ),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PlaceMetadataPreview() {
    WayspotTheme {
        PlaceMetadata(
            place = PlaceInfo(
                id = "1",
                title = "Monserrate",
                category = "Turismo",
                location = "Bogotá, Colombia",
                rating = 4.7,
                imageUrl = null,
                description = "Lugar turístico ubicado en Bogotá"
            ),
            onShareClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PlaceLocationPreview() {
    WayspotTheme {
        PlaceLocation(
            location = "Bogotá, Colombia"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PlaceRatingPreview() {
    WayspotTheme {
        PlaceRating(
            rating = 4.7
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RatingStarsPreview() {
    WayspotTheme {
        RatingStars(
            rating = 4,
            size = 16.dp
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SharePlaceButtonPreview() {
    WayspotTheme {
        SharePlaceButton(
            onClick = {}
        )
    }
}