package com.example.wayspot.ui.screens.placedetail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.example.wayspot.data.model.ReviewInfo
import com.example.wayspot.ui.theme.WayspotTheme

/** Lista de reseñas asociadas al lugar seleccionado. */
@Composable
internal fun RecentReviewsSection(
    reviews: List<ReviewInfo>,
    onSeeAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        RecentReviewsHeader(
            onSeeAllClick = onSeeAllClick
        )

        reviews.forEach { review ->
            PlaceReviewCard(
                review = review,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )
        }
    }
}

@Composable
private fun RecentReviewsHeader(
    onSeeAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
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

        TextButton(
            onClick = onSeeAllClick
        ) {
            Text(
                text = stringResource(R.string.see_all)
            )
        }
    }
}

@Composable
private fun PlaceReviewCard(
    review: ReviewInfo,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            ReviewHeader(
                review = review
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = review.description,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun ReviewHeader(
    review: ReviewInfo,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = review.title,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        RatingStars(
            rating = review.rating,
            size = 13.dp
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RecentReviewsSectionPreview() {
    WayspotTheme {
        RecentReviewsSection(
            reviews = listOf(
                ReviewInfo(
                    id = "1",
                    rating = 5,
                    title = "Excelente lugar",
                    description = "Una experiencia muy buena y con una gran vista.",
                    userId = "1",
                    placeId = "1"
                )
            ),
            onSeeAllClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RecentReviewsHeaderPreview() {
    WayspotTheme {
        RecentReviewsHeader(
            onSeeAllClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PlaceReviewCardPreview() {
    WayspotTheme {
        PlaceReviewCard(
            review = ReviewInfo(
                id = "1",
                rating = 5,
                title = "Excelente lugar",
                description = "Una experiencia muy buena y con una gran vista.",
                userId = "1",
                placeId = "1"
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ReviewHeaderPreview() {
    WayspotTheme {
        ReviewHeader(
            review = ReviewInfo(
                id = "1",
                rating = 5,
                title = "Excelente lugar",
                description = "Una experiencia muy buena y con una gran vista.",
                userId = "1",
                placeId = "1"
            )
        )
    }
}