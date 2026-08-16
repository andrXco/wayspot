package com.example.wayspot.ui.screens.placedetail.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wayspot.R
import com.example.wayspot.model.PlaceReview
import com.example.wayspot.model.Places
import com.example.wayspot.ui.preview.PreviewDataPopular
import com.example.wayspot.ui.theme.EstrellaAmarilla
import com.example.wayspot.ui.theme.WayspotTheme

@Composable
fun PlaceDetailBody(
    place: Places,
    onShareClick: () -> Unit,
    onWriteReviewClick: () -> Unit,
    onSeeAllReviewsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 24.dp)) {
            PlaceMetadata(place = place, onShareClick = onShareClick)
            Spacer(modifier = Modifier.height(16.dp))
            PlaceInfoRow(place = place)
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(R.string.description_title),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(place.detail.descriptionRes),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 22.sp
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onWriteReviewClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = stringResource(R.string.button_write_review))
            }
            if (place.detail.recentReviews.isNotEmpty()) {
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.recent_reviews_title),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(onClick = onSeeAllReviewsClick) {
                        Text(text = stringResource(R.string.see_all))
                    }
                }
                place.detail.recentReviews.forEach { review ->
                    PlaceReviewCard(review = review, modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlaceDetailBodyPreview() {
    WayspotTheme {
        PlaceDetailBody(
            place = PreviewDataPopular.samplePlaces1,
            onShareClick = {},
            onWriteReviewClick = {},
            onSeeAllReviewsClick = {}
        )
    }
}

@Composable
private fun PlaceMetadata(
    place: Places,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stringResource(place.ubicacionRes),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                repeat(5) { starIndex ->
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = if (starIndex < place.rating.toInt()) EstrellaAmarilla else MaterialTheme.colorScheme.outlineVariant,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.rating_value, place.rating),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stringResource(R.string.reviews_count, place.detail.reviewCount),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp
                )
            }
        }
        IconButton(
            onClick = onShareClick,
            modifier = Modifier
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
}

@Composable
private fun PlaceInfoRow(place: Places, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        PlaceInfoCard(
            icon = Icons.Default.AccessTime,
            textRes = place.detail.durationRes,
            modifier = Modifier.weight(1f)
        )
        PlaceInfoCard(
            icon = Icons.Default.ConfirmationNumber,
            textRes = place.detail.priceRes,
            modifier = Modifier.weight(1f)
        )
        PlaceInfoCard(
            icon = Icons.Default.Place,
            textRes = place.detail.altitudeRes,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun PlaceInfoCard(
    icon: ImageVector,
    textRes: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(80.dp),
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(textRes),
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun PlaceReviewCard(review: PlaceReview, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(MaterialTheme.colorScheme.primary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.review_avatar_initials),
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(text = stringResource(review.userNameRes), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Row {
                            repeat(5) { starIndex ->
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = if (starIndex < review.rating) EstrellaAmarilla else MaterialTheme.colorScheme.outlineVariant,
                                    modifier = Modifier.size(13.dp)
                                )
                            }
                        }
                    }
                }
                Text(
                    text = stringResource(review.dateRes),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(review.commentRes),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp
            )
        }
    }
}
