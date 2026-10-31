package com.example.wayspot.ui.screens.placedetail.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wayspot.R
import com.example.wayspot.data.model.review.ReviewInfo
import com.example.wayspot.ui.components.ProfileAvatar
import com.example.wayspot.ui.theme.WayspotTheme

@Composable
internal fun RecentReviewsHeader(
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

    }
}

@Composable
internal fun PlaceReviewCard(
    review: ReviewInfo,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.14f)),
        tonalElevation = 1.dp,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ReviewAuthorRow(review = review)
            ReviewTitleAndRating(review = review)
            Text(
                text = review.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp
            )
            review.reviewDate?.takeIf(String::isNotBlank)?.let { date ->
                Text(
                    text = date,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.78f)
                )
            }
        }
    }
}

@Composable
private fun ReviewAuthorRow(
    review: ReviewInfo,
    modifier: Modifier = Modifier
) {
    val authorName = review.authorName?.takeIf(String::isNotBlank)
        ?: review.authorUsername?.takeIf(String::isNotBlank)
        ?: stringResource(R.string.label_user)
    val avatarDescription = stringResource(
        R.string.review_author_avatar_content_description,
        authorName
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProfileAvatar(
            avatarUrl = review.authorAvatarUrl,
            initials = authorName.take(1).uppercase(),
            contentDescription = avatarDescription,
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = authorName,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )
            review.authorUsername?.takeIf(String::isNotBlank)?.let { username ->
                Text(
                    text = stringResource(R.string.profile_username_format, username),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
private fun ReviewTitleAndRating(
    review: ReviewInfo,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        review.title?.takeIf(String::isNotBlank)?.let { title ->
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 2
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            RatingStars(
                rating = review.rating,
                size = 15.dp
            )
            Text(
                text = stringResource(R.string.review_rating_value, review.rating),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RecentReviewsHeaderPreview() {
    WayspotTheme {
        RecentReviewsHeader()
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
                placeId = "1",
                authorName = "Valentina García",
                authorUsername = "valentina_viaja"
            ),
            onClick = {}
        )
    }
}
