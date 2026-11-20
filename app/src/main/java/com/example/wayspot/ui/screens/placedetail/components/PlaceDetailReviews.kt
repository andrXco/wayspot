package com.example.wayspot.ui.screens.placedetail.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wayspot.R
import com.example.wayspot.data.local.PreviewData
import com.example.wayspot.data.model.review.ReviewInfo
import com.example.wayspot.ui.components.ProfileAvatar
import com.example.wayspot.ui.components.WayspotImage
import com.example.wayspot.ui.theme.WayspotTheme

@Composable
internal fun RecentReviewsHeader(
    reviewCount: Int,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
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
            Text(
                text = pluralStringResource(
                    R.plurals.recent_reviews_count,
                    reviewCount,
                    reviewCount
                ),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Text(
            text = stringResource(R.string.recent_reviews_subtitle),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
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
            .clickable(onClick = onClick)
            .testTag("e2eReview_${review.id}"),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.08f)),
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ReviewAuthorRow(
                    review = review,
                    modifier = Modifier.weight(1f)
                )
                VerticalDivider(
                    modifier = Modifier.height(40.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )
                ReviewRatingSummary(
                    rating = review.rating,
                    modifier = Modifier.padding(start = 12.dp, end = 2.dp)
                )
            }
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.65f)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                ReviewSummary(review = review, modifier = Modifier.weight(1f))
                if (review.photoUrls.isNotEmpty()) {
                    ReviewPhotos(review = review)
                }
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
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 10.dp)
        ) {
            Text(
                text = authorName,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )
            val username = review.authorUsername?.takeIf(String::isNotBlank)
            val date = review.reviewDate?.takeIf(String::isNotBlank)
            val metadata = when {
                username != null && date != null -> stringResource(
                    R.string.review_author_metadata,
                    stringResource(R.string.profile_username_format, username),
                    date
                )
                username != null -> stringResource(R.string.profile_username_format, username)
                date != null -> date
                else -> null
            }
            metadata?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
private fun ReviewRatingSummary(
    rating: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = stringResource(R.string.rating_value, rating.toDouble()),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        RatingStars(rating = rating, size = 11.dp)
        Text(
            text = stringResource(
                when (rating.coerceIn(1, 5)) {
                    1 -> R.string.review_rating_summary_1
                    2 -> R.string.review_rating_summary_2
                    3 -> R.string.review_rating_summary_3
                    4 -> R.string.review_rating_summary_4
                    else -> R.string.review_rating_summary_5
                }
            ),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ReviewSummary(
    review: ReviewInfo,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        review.title?.takeIf(String::isNotBlank)?.let { title ->
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 2
            )
        }
        Text(
            text = review.description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 18.sp,
            maxLines = 3
        )
        Text(
            text = stringResource(R.string.review_summary_link),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun ReviewPhotos(
    review: ReviewInfo,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.width(94.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        review.photoUrls.take(2).forEachIndexed { index, photoUrl ->
            WayspotImage(
                imageModel = photoUrl,
                contentDescription = stringResource(
                    R.string.review_photo_content_description,
                    index + 1
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RecentReviewsHeaderPreview() {
    WayspotTheme {
        RecentReviewsHeader(reviewCount = 1)
    }
}

@Preview(showBackground = true)
@Composable
private fun PlaceReviewCardPreview() {
    WayspotTheme {
        PlaceReviewCard(
            review = PreviewData.placeDetailReview,
            onClick = {}
        )
    }
}
