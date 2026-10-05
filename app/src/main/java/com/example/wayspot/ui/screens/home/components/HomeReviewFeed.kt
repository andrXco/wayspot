package com.example.wayspot.ui.screens.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.rounded.BookmarkBorder
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wayspot.R
import com.example.wayspot.data.model.HomeReview
import com.example.wayspot.data.model.HomeRules
import com.example.wayspot.data.model.PlaceInfo
import com.example.wayspot.data.local.PreviewData
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.components.WayspotImage
import com.example.wayspot.ui.theme.WayspotTheme

@Composable
fun HomeReviewsHeader(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = stringResource(R.string.home_reviews_title),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            text = stringResource(R.string.home_reviews_subtitle),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
fun HomeReviewsEmptyState(
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(R.string.home_reviews_empty),
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = MaterialTheme.typography.bodyMedium
    )
}

/** Tarjeta de reseña que recibe el estado de interacción resuelto por la pantalla de inicio. */
@Composable
fun HomeReviewCard(
    review: HomeReview,
    place: PlaceInfo,
    likeCount: Int,
    commentCount: Int,
    isLiked: Boolean,
    isExpanded: Boolean,
    isShared: Boolean,
    isSaved: Boolean,
    onPlaceClick: () -> Unit,
    onLikeClick: () -> Unit,
    onExpandClick: () -> Unit,
    onCommentClick: () -> Unit,
    onShareClick: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val authorName = stringResource(review.authorNameRes)
    val placeTitle = place.title
    val rating = HomeRules.normalizedReviewRating(review.rating)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)
        )
    ) {
        Column(
            modifier = Modifier.padding(vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            HomeReviewAuthorRow(
                review = review,
                authorName = authorName,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            HomeReviewPlacePill(
                place = place,
                placeTitle = placeTitle,
                onClick = onPlaceClick,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            HomeReviewRating(
                rating = rating,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Text(
                text = stringResource(review.bodyRes),
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.onSurface,
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
                        if (isExpanded) {
                            R.string.home_review_show_less
                        } else {
                            R.string.home_review_show_more
                        }
                    ),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            HomeReviewPhotos(
                review = review,
                placeTitle = placeTitle,
                modifier = Modifier.fillMaxWidth()
            )
            HomeReviewActions(
                authorName = authorName,
                placeTitle = placeTitle,
                likeCount = likeCount,
                commentCount = commentCount,
                isLiked = isLiked,
                isShared = isShared,
                isSaved = isSaved,
                onLikeClick = onLikeClick,
                onCommentClick = onCommentClick,
                onShareClick = onShareClick,
                onSaveClick = onSaveClick,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
private fun HomeReviewAuthorRow(
    review: HomeReview,
    authorName: String,
    modifier: Modifier = Modifier
) {
    val (avatarColor, avatarContentColor) = when (review.id.hashCode().mod(3)) {
        0 -> MaterialTheme.colorScheme.primary to MaterialTheme.colorScheme.onPrimary
        1 -> MaterialTheme.colorScheme.tertiary to MaterialTheme.colorScheme.onTertiary
        else -> MaterialTheme.colorScheme.secondary to MaterialTheme.colorScheme.onSecondary
    }
    val avatarContentDescription = stringResource(
        R.string.home_review_avatar_content_description,
        authorName
    )
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(38.dp),
            shape = CircleShape,
            color = avatarColor,
            contentColor = avatarContentColor
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(review.authorInitialsRes),
                    modifier = Modifier.clearAndSetSemantics {
                        contentDescription = avatarContentDescription
                    },
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = authorName,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = stringResource(review.authorHandleRes),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelSmall
            )
        }
        Text(
            text = stringResource(review.dateRes),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
private fun HomeReviewPlacePill(
    place: PlaceInfo,
    placeTitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.clickable { onClick() },
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
                contentDescription = stringResource(
                    R.string.home_review_place_content_description,
                    placeTitle
                ),
                modifier = Modifier.size(15.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = placeTitle,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = place.location,
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
private fun HomeReviewRating(
    rating: Int,
    modifier: Modifier = Modifier
) {
    val ratingColor = if (isSystemInDarkTheme()) {
        MaterialTheme.colorScheme.tertiary
    } else {
        MaterialTheme.colorScheme.tertiaryContainer
    }
    val ratingContentDescription = stringResource(
        R.string.home_review_rating_content_description,
        rating,
        HomeRules.MAX_REVIEW_RATING
    )
    Row(
        modifier = modifier.clearAndSetSemantics {
            contentDescription = ratingContentDescription
        },
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        repeat(HomeRules.MAX_REVIEW_RATING) { index ->
            Icon(
                imageVector = if (index < rating) Icons.Rounded.Star else Icons.Rounded.StarBorder,
                contentDescription = null,
                tint = ratingColor,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun HomeReviewPhotos(
    review: HomeReview,
    placeTitle: String,
    modifier: Modifier = Modifier
) {
    val imageWidth = if (review.photoUrls.size > 2) 102.dp else 156.dp
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        itemsIndexed(
            items = review.photoUrls,
            key = { index, url -> "$index-$url" }
        ) { index, url ->
            WayspotImage( // PORQUE NO TIENE EL ASYNCIMAGE
                imageModel = url,
                contentDescription = stringResource(
                    R.string.home_review_photo_content_description,
                    index + 1,
                    placeTitle
                ),
                modifier = Modifier
                    .width(imageWidth)
                    .height(160.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop,
                placeholderResId = R.drawable.branding_logo_claro_wayspot,
                errorResId = R.drawable.branding_logo_claro_wayspot
            )
        }
    }
}

@Composable
private fun HomeReviewActions(
    authorName: String,
    placeTitle: String,
    likeCount: Int,
    commentCount: Int,
    isLiked: Boolean,
    isShared: Boolean,
    isSaved: Boolean,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit,
    onShareClick: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HomeReviewAction(
            image = if (isLiked) Icons.Filled.Favorite else Icons.Rounded.FavoriteBorder,
            contentDescription = stringResource(
                if (isLiked) {
                    R.string.home_review_unlike_content_description
                } else {
                    R.string.home_review_like_content_description
                },
                authorName
            ),
            tint = if (isLiked) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurfaceVariant,
            onClick = onLikeClick
        )
        Text(
            text = stringResource(R.string.home_review_action_count, likeCount),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelMedium
        )
        Spacer(modifier = Modifier.width(10.dp))
        HomeReviewAction(
            image = Icons.Rounded.ChatBubbleOutline,
            contentDescription = stringResource(
                R.string.home_review_comment_content_description,
                authorName
            ),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            onClick = onCommentClick
        )
        Text(
            text = stringResource(R.string.home_review_action_count, commentCount),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelMedium
        )
        Spacer(modifier = Modifier.weight(1f))
        HomeReviewAction(
            image = Icons.Rounded.Share,
            contentDescription = stringResource(
                if (isShared) {
                    R.string.home_review_shared_content_description
                } else {
                    R.string.home_review_share_content_description
                },
                authorName
            ),
            tint = if (isShared) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
            onClick = onShareClick
        )
        HomeReviewAction(
            image = if (isSaved) Icons.Filled.Bookmark else Icons.Rounded.BookmarkBorder,
            contentDescription = stringResource(
                if (isSaved) {
                    R.string.home_review_unsave_content_description
                } else {
                    R.string.home_review_save_content_description
                },
                placeTitle
            ),
            tint = if (isSaved) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
            onClick = onSaveClick
        )
    }
}

@Composable
private fun HomeReviewAction(
    image: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    tint: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.size(38.dp)
    ) {
        Icon(
            imageVector = image,
            contentDescription = contentDescription,
            tint = tint,
            modifier = Modifier.size(19.dp)
        )
    }
}

@WayspotMultiPreview
@Composable
private fun HomeReviewPhotosPreview() {
    WayspotTheme {
        Surface {
            HomeReviewPhotos(
                review = PreviewData.homeReviews.first(),
                placeTitle = "Cerro Monserrate"
            )
        }
    }
}
