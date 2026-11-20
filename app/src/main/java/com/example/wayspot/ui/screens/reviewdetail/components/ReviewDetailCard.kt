package com.example.wayspot.ui.screens.reviewdetail.components

import android.text.format.DateUtils
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.wayspot.R
import com.example.wayspot.data.model.place.PlaceInfo
import com.example.wayspot.data.model.review.ReviewInfo
import com.example.wayspot.data.model.review.ReviewRules
import com.example.wayspot.data.model.user.EditProfileRules
import com.example.wayspot.data.model.user.UserInfo
import com.example.wayspot.ui.components.ProfileAvatar

@Composable
fun ReviewDetailCard(
    review: ReviewInfo,
    place: PlaceInfo?,
    author: UserInfo?,
    canLike: Boolean,
    canComment: Boolean,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit,
    onPlaceClick: (String) -> Unit,
    onAuthorClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val authorName = author?.name?.takeIf(String::isNotBlank)
        ?: review.authorName.orEmpty()
    val avatarUrl = author?.avatarUrl ?: review.authorAvatarUrl
    val reviewDate = review.createdAtMillis?.let { createdAtMillis ->
        DateUtils.getRelativeTimeSpanString(
            createdAtMillis,
            System.currentTimeMillis(),
            DateUtils.MINUTE_IN_MILLIS
        ).toString()
    } ?: review.reviewDate

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.16f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (authorName.isNotBlank()) {
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .clickable(
                                    enabled = author != null,
                                    role = Role.Button,
                                    onClick = onAuthorClick
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ProfileAvatar(
                                avatarUrl = avatarUrl,
                                initials = EditProfileRules.initialsForName(authorName),
                                contentDescription = stringResource(
                                    R.string.review_author_avatar_content_description,
                                    authorName
                                ),
                                modifier = Modifier.size(38.dp)
                            )
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 10.dp),
                                verticalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Text(
                                    text = authorName,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 1
                                )
                                reviewDate?.takeIf(String::isNotBlank)?.let { date ->
                                    Text(
                                        text = date,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }

                    place?.category?.takeIf(String::isNotBlank)?.let { category ->
                        Surface(
                            shape = RoundedCornerShape(24.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ) {
                            Text(
                                text = category,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }

                review.title?.takeIf(String::isNotBlank)?.let { title ->
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                }

                place?.let { placeInfo ->
                    Surface(
                        modifier = Modifier.clickable(
                            role = Role.Button,
                            onClick = { onPlaceClick(placeInfo.id) }
                        ),
                        shape = RoundedCornerShape(24.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.LocationOn,
                                contentDescription = null,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = placeInfo.title,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            if (placeInfo.location.isNotBlank()) {
                                Text(
                                    text = placeInfo.location,
                                    modifier = Modifier.padding(start = 5.dp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    style = MaterialTheme.typography.labelSmall,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }

                ReviewDetailPhotoGallery(
                    photoUrls = review.photoUrls,
                    placeTitle = place?.title ?: authorName,
                    modifier = Modifier.fillMaxWidth()
                )

                ReviewDetailRating(
                    rating = review.rating,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = review.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            ReviewDetailActions(
                authorName = authorName,
                likeCount = review.likesCount,
                isLiked = review.liked,
                canLike = canLike,
                canComment = canComment,
                onLikeClick = onLikeClick,
                onCommentClick = onCommentClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ReviewDetailRating(
    rating: Int,
    modifier: Modifier = Modifier
) {
    val normalizedRating = rating.coerceIn(ReviewRules.MIN_RATING, ReviewRules.MAX_RATING)
    val ratingDescription = stringResource(R.string.review_rating_value, normalizedRating)

    Row(
        modifier = modifier.clearAndSetSemantics {
            contentDescription = ratingDescription
        },
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(ReviewRules.MAX_RATING) { index ->
            Icon(
                imageVector = if (index < normalizedRating) {
                    Icons.Rounded.Star
                } else {
                    Icons.Rounded.StarBorder
                },
                contentDescription = null,
                tint = if (index < normalizedRating) {
                    MaterialTheme.colorScheme.tertiary
                } else {
                    MaterialTheme.colorScheme.outline
                },
                modifier = Modifier.size(18.dp)
            )
        }
        Text(
            text = stringResource(
                R.string.review_detail_rating_display,
                normalizedRating.toFloat()
            ),
            modifier = Modifier.padding(start = 6.dp),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
