package com.example.wayspot.ui.screens.profile.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wayspot.R
import com.example.wayspot.data.local.PreviewData
import com.example.wayspot.data.local.PreviewDataPopular
import com.example.wayspot.data.model.review.ReviewFeedItem
import com.example.wayspot.data.model.review.ReviewInfo
import com.example.wayspot.data.model.review.ReviewRules
import com.example.wayspot.data.model.user.UserProfile
import com.example.wayspot.ui.components.ProfileAvatar
import com.example.wayspot.ui.screens.home.components.HomeReviewCard
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.theme.EstrellaAmarilla
import com.example.wayspot.ui.theme.WayspotTheme

/** Resume una reseña realizada por el usuario. */
@Composable
fun ProfileReviewItem(
    review: ReviewInfo,
    author: UserProfile,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    feedItem: ReviewFeedItem? = null,
    likeCount: Int = 0,
    isLiked: Boolean = false,
    isExpanded: Boolean = false,
    isShared: Boolean = false,
    onAuthorClick: () -> Unit = {},
    onPlaceClick: () -> Unit = {},
    onLikeClick: () -> Unit = {},
    onExpandClick: () -> Unit = {},
    onCommentClick: () -> Unit = {},
    onShareClick: () -> Unit = {}
) {
    if (feedItem != null) {
        HomeReviewCard(
            item = feedItem,
            likeCount = likeCount,
            isLiked = isLiked,
            isExpanded = isExpanded,
            isShared = isShared,
            onReviewClick = onClick,
            onAuthorClick = onAuthorClick,
            onPlaceClick = onPlaceClick,
            onLikeClick = onLikeClick,
            onExpandClick = onExpandClick,
            onCommentClick = onCommentClick,
            onShareClick = onShareClick,
            modifier = modifier
        )
        return
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(
                horizontal = 16.dp,
                vertical = 6.dp
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        border = BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val authorName = review.authorName ?: author.name
                ProfileAvatar(
                    avatarUrl = review.authorAvatarUrl ?: author.avatarUrl,
                    initials = authorName.take(1).uppercase(),
                    contentDescription = stringResource(
                        R.string.home_review_avatar_content_description,
                        authorName
                    ),
                    modifier = Modifier.size(30.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(
                        R.string.profile_username_format,
                        review.authorUsername?.takeIf(String::isNotBlank) ?: author.username
                    ),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = review.title.orEmpty(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                repeat(ReviewRules.MAX_RATING) { index ->
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = if (index < review.rating) {
                            EstrellaAmarilla
                        } else {
                            MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
                        },
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = review.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 18.sp
            )
        }
    }
}

@WayspotMultiPreview
@Composable
private fun ProfileReviewItemPreview() {
    WayspotTheme {
        val review = PreviewData.reviewDetailReview
        ProfileReviewItem(
            review = review,
            author = PreviewData.userProfile,
            feedItem = ReviewFeedItem(
                review = review,
                place = PreviewDataPopular.previewPlaceInfo,
                author = PreviewData.reviewDetailAuthor
            ),
            onClick = {}
        )
    }
}
