package com.example.wayspot.ui.screens.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.rounded.ChatBubbleOutline
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wayspot.R
import com.example.wayspot.data.model.HomeRules
import com.example.wayspot.data.model.ReviewFeedItem

/** Mantiene la estructura visual del feed y recibe la reseña publicada desde el backend. */
@Composable
fun HomeReviewCard(
    item: ReviewFeedItem,
    likeCount: Int,
    isLiked: Boolean,
    isExpanded: Boolean,
    isShared: Boolean,
    onReviewClick: () -> Unit,
    onAuthorClick: () -> Unit,
    onPlaceClick: () -> Unit,
    onLikeClick: () -> Unit,
    onExpandClick: () -> Unit,
    onCommentClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val rating = HomeRules.normalizedReviewRating(item.review.rating)
    val authorName = item.author.name

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onReviewClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.12f))
    ) {
        Column(
            modifier = Modifier.padding(vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            HomeReviewAuthorRow(
                name = authorName,
                username = item.author.username,
                onClick = onAuthorClick,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            HomeReviewPlacePill(
                title = item.place.title,
                location = item.place.location,
                onClick = onPlaceClick,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            HomeReviewRating(
                rating = rating,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Text(
                text = item.review.title,
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = item.review.description,
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 21.sp,
                maxLines = if (isExpanded) Int.MAX_VALUE else HomeRules.MAX_COLLAPSED_REVIEW_LINES,
                overflow = TextOverflow.Ellipsis
            )
            TextButton(
                onClick = onExpandClick,
                modifier = Modifier.padding(horizontal = 8.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
            ) {
                Text(
                    text = stringResource(
                        if (isExpanded) R.string.home_review_show_less
                        else R.string.home_review_show_more
                    ),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            HomeReviewActions(
                authorName = authorName,
                likeCount = likeCount,
                isLiked = isLiked,
                isShared = isShared,
                onLikeClick = onLikeClick,
                onCommentClick = onCommentClick,
                onShareClick = onShareClick,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
private fun HomeReviewAuthorRow(
    name: String,
    username: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val avatarDescription = stringResource(R.string.home_review_avatar_content_description, name)
    Row(
        modifier = modifier.fillMaxWidth().clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(38.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = name.take(1).uppercase(),
                    modifier = Modifier.clearAndSetSemantics {
                        contentDescription = avatarDescription
                    },
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = name,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = stringResource(R.string.profile_username_format, username),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
private fun HomeReviewPlacePill(
    title: String,
    location: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.38f),
        contentColor = MaterialTheme.colorScheme.primary
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Rounded.LocationOn,
                contentDescription = stringResource(R.string.home_review_place_content_description, title),
                modifier = Modifier.size(15.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = title, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
            Text(
                text = location,
                modifier = Modifier.padding(start = 5.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun HomeReviewRating(rating: Int, modifier: Modifier = Modifier) {
    val ratingDescription = stringResource(
        R.string.home_review_rating_content_description,
        rating,
        HomeRules.MAX_REVIEW_RATING
    )
    Row(
        modifier = modifier.clearAndSetSemantics { contentDescription = ratingDescription },
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        repeat(HomeRules.MAX_REVIEW_RATING) { index ->
            Icon(
                imageVector = if (index < rating) Icons.Rounded.Star else Icons.Rounded.StarBorder,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun HomeReviewActions(
    authorName: String,
    likeCount: Int,
    isLiked: Boolean,
    isShared: Boolean,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onLikeClick) {
            Icon(
                imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Rounded.FavoriteBorder,
                contentDescription = stringResource(
                    if (isLiked) R.string.home_review_unlike_content_description
                    else R.string.home_review_like_content_description,
                    authorName
                ),
                tint = if (isLiked) MaterialTheme.colorScheme.tertiary
                    else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = stringResource(R.string.home_review_action_count, likeCount),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(10.dp))
        IconButton(onClick = onCommentClick) {
            Icon(
                imageVector = Icons.Rounded.ChatBubbleOutline,
                contentDescription = stringResource(R.string.home_review_comment_content_description, authorName),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = onShareClick) {
            Icon(
                imageVector = Icons.Rounded.Share,
                contentDescription = stringResource(
                    if (isShared) R.string.home_review_shared_content_description
                    else R.string.home_review_share_content_description,
                    authorName
                ),
                tint = if (isShared) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
