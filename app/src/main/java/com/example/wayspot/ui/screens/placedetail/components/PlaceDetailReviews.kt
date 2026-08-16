package com.example.wayspot.ui.screens.placedetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wayspot.R
import com.example.wayspot.model.PlaceReview
import com.example.wayspot.ui.preview.PreviewDataPopular
import com.example.wayspot.ui.theme.WayspotTheme

@Composable
internal fun RecentReviewsSection(
    reviews: List<PlaceReview>,
    onSeeAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        RecentReviewsHeader(onSeeAllClick = onSeeAllClick)
        reviews.forEach { review ->
            PlaceReviewCard(review = review, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
private fun RecentReviewsHeader(onSeeAllClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.recent_reviews_title),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        TextButton(onClick = onSeeAllClick) {
            Text(text = stringResource(R.string.see_all))
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
            ReviewHeader(review = review)
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

@Composable
private fun ReviewHeader(review: PlaceReview, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ReviewAuthor(review = review)
        Text(
            text = stringResource(review.dateRes),
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ReviewAuthor(review: PlaceReview, modifier: Modifier = Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
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
            Text(
                text = stringResource(review.userNameRes),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            RatingStars(rating = review.rating, size = 13.dp)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RecentReviewsSectionPreview() {
    WayspotTheme {
        RecentReviewsSection(
            reviews = PreviewDataPopular.samplePlaces1.detail.recentReviews,
            onSeeAllClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RecentReviewsHeaderPreview() {
    WayspotTheme {
        RecentReviewsHeader(onSeeAllClick = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun PlaceReviewCardPreview() {
    WayspotTheme {
        PlaceReviewCard(review = PreviewDataPopular.samplePlaces1.detail.recentReviews.first())
    }
}

@Preview(showBackground = true)
@Composable
private fun ReviewHeaderPreview() {
    WayspotTheme {
        ReviewHeader(review = PreviewDataPopular.samplePlaces1.detail.recentReviews.first())
    }
}

@Preview(showBackground = true)
@Composable
private fun ReviewAuthorPreview() {
    WayspotTheme {
        ReviewAuthor(review = PreviewDataPopular.samplePlaces1.detail.recentReviews.first())
    }
}
