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
import androidx.compose.ui.unit.sp
import com.example.wayspot.R
import com.example.wayspot.data.model.Places
import com.example.wayspot.data.local.PreviewDataPopular
import com.example.wayspot.ui.theme.EstrellaAmarilla
import com.example.wayspot.ui.theme.WayspotTheme

@Composable
internal fun PlaceMetadata(
    place: Places,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PlaceLocationAndRating(place = place)
        SharePlaceButton(onClick = onShareClick)
    }
}

@Composable
private fun PlaceLocationAndRating(place: Places, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        PlaceLocation(locationRes = place.ubicacionRes)
        Spacer(modifier = Modifier.height(8.dp))
        PlaceRating(rating = place.rating, reviewCount = place.detail.reviewCount)
    }
}

@Composable
private fun PlaceLocation(locationRes: Int, modifier: Modifier = Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = stringResource(locationRes),
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun PlaceRating(
    rating: Double,
    reviewCount: Int,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        RatingStars(rating = rating.toInt(), size = 16.dp)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = stringResource(R.string.rating_value, rating), fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = stringResource(R.string.reviews_count, reviewCount),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 13.sp
        )
    }
}

@Composable
internal fun RatingStars(rating: Int, size: Dp, modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        repeat(5) { starIndex ->
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = if (starIndex < rating) EstrellaAmarilla else MaterialTheme.colorScheme.outlineVariant,
                modifier = Modifier.size(size)
            )
        }
    }
}

@Composable
private fun SharePlaceButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(40.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
    ) {
        Icon(
            imageVector = Icons.Default.Share,
            contentDescription = stringResource(R.string.share_content_description),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PlaceMetadataPreview() {
    WayspotTheme {
        PlaceMetadata(place = PreviewDataPopular.samplePlaces1, onShareClick = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun PlaceLocationAndRatingPreview() {
    WayspotTheme {
        PlaceLocationAndRating(place = PreviewDataPopular.samplePlaces1)
    }
}

@Preview(showBackground = true)
@Composable
private fun PlaceLocationPreview() {
    WayspotTheme {
        PlaceLocation(locationRes = PreviewDataPopular.samplePlaces1.ubicacionRes)
    }
}

@Preview(showBackground = true)
@Composable
private fun PlaceRatingPreview() {
    WayspotTheme {
        PlaceRating(
            rating = PreviewDataPopular.samplePlaces1.rating,
            reviewCount = PreviewDataPopular.samplePlaces1.detail.reviewCount
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RatingStarsPreview() {
    WayspotTheme {
        RatingStars(rating = PreviewDataPopular.samplePlaces1.rating.toInt(), size = 16.dp)
    }
}

@Preview(showBackground = true)
@Composable
private fun SharePlaceButtonPreview() {
    WayspotTheme {
        SharePlaceButton(onClick = {})
    }
}
