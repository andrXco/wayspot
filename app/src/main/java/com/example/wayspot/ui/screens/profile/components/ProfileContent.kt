package com.example.wayspot.ui.screens.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.material3.TextButton
import com.example.wayspot.data.model.review.ReviewInfo
import com.example.wayspot.data.model.review.ReviewFeedItem
import com.example.wayspot.data.model.user.UserProfile
import androidx.compose.ui.res.stringResource
import com.example.wayspot.R
import com.example.wayspot.data.local.PreviewData
import com.example.wayspot.data.local.PreviewDataPopular
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.theme.WayspotTheme


/** Organiza la cabecera del perfil, sus accesos y el historial de reseñas en una lista con claves estables. */
@Composable
fun ProfileContent(
    user: UserProfile,
    reviews: List<ReviewInfo>,
    onEditProfileClick: () -> Unit,
    onSavedPlacesClick: () -> Unit,
    onSignOutClick: () -> Unit,
    onReviewClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    errorMessage: String? = null,
    isOwnProfile: Boolean = true,
    onBackClick: (() -> Unit)? = null,
    reviewFeedItems: List<ReviewFeedItem> = emptyList(),
    reviewLikeCounts: Map<String, Int> = emptyMap(),
    likedReviewIds: Set<String> = emptySet(),
    expandedReviewIds: Set<String> = emptySet(),
    sharedReviewIds: Set<String> = emptySet(),
    onReviewLikeClick: (String) -> Unit = {},
    onReviewExpandClick: (String) -> Unit = {},
    onReviewCommentClick: (String) -> Unit = {},
    onReviewShareClick: (String) -> Unit = {},
    onReviewPlaceClick: (String) -> Unit = {},
    onFollowersClick: () -> Unit = {},
    onFollowingClick: () -> Unit = {},
    canFollow: Boolean = false,
    isFollowing: Boolean = false,
    isFollowActionInProgress: Boolean = false,
    hasFollowError: Boolean = false,
    onFollowClick: () -> Unit = {}

) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.secondary
                            )
                        )
                    )
                    .statusBarsPadding()
                    .padding(
                        top = 24.dp,
                        bottom = 20.dp
                    )
            ) {

                onBackClick?.let { back ->
                    TextButton(
                        onClick = back,
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.volver),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }

                ProfileHeader(
                    user = user,
                    modifier = Modifier.fillMaxWidth()
                )

                ProfileStats(
                    stats = user.stats.copy(reviews = reviews.size),
                    onFollowersClick = onFollowersClick,
                    onFollowingClick = onFollowingClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 12.dp,
                            vertical = 12.dp
                        )
                )

                if (!isOwnProfile && canFollow) {
                    Button(
                        onClick = onFollowClick,
                        enabled = !isFollowActionInProgress,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.onPrimary,
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            text = stringResource(
                                if (isFollowing) R.string.profile_following_action
                                else R.string.profile_follow_action
                            )
                        )
                    }
                    if (hasFollowError) {
                        Text(
                            text = stringResource(R.string.profile_follow_error),
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        if (isOwnProfile) {
                item {
                    ProfileEditButton(
                        onClick = onEditProfileClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 16.dp,
                                vertical = 12.dp
                            )
                    )
                }
        }


        if (isOwnProfile) {
            item {
                ProfileTabs(
                    selectedTabIndex = 0,
                    onTabSelected = { index ->
                        if (index == 1) {
                            onSavedPlacesClick()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 4.dp,
                            bottom = 4.dp
                        )
                )
            }
        }


        if (isLoading) {
            item(key = "reviews-loading") { CircularProgressIndicator() }
        }
        errorMessage?.let { message ->
            item(key = "reviews-error") {
                Text(message, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(16.dp))
            }
        }
        if (reviews.isEmpty() && !isLoading && errorMessage == null) {
            item(key = "reviews-empty") {
                Text(
                    text = stringResource(R.string.profile_reviews_empty),
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        items(
            items = reviews,
            key = {
                it.id
            }
        ) { review ->
            val feedItem = reviewFeedItems.firstOrNull { it.review.id == review.id }
            ProfileReviewItem(
                review = review,
                author = user,
                feedItem = feedItem.takeIf { isOwnProfile },
                likeCount = reviewLikeCounts[review.id] ?: 0,
                isLiked = review.id in likedReviewIds,
                isExpanded = review.id in expandedReviewIds,
                isShared = review.id in sharedReviewIds,
                onClick = { onReviewClick(review.id) },
                onAuthorClick = {},
                onPlaceClick = {
                    feedItem?.place?.id?.let(onReviewPlaceClick)
                },
                onLikeClick = { onReviewLikeClick(review.id) },
                onExpandClick = { onReviewExpandClick(review.id) },
                onCommentClick = { onReviewCommentClick(review.id) },
                onShareClick = { onReviewShareClick(review.id) },
                modifier = if (feedItem != null && isOwnProfile) {
                    Modifier.padding(horizontal = 16.dp, vertical = 7.dp)
                } else {
                    Modifier.fillMaxWidth()
                }
            )
        }
        if (isOwnProfile) {
            item {
                ProfileSignOutButton(
                    onClick = onSignOutClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 32.dp,
                            vertical = 16.dp
                        )
                )
            }
        }
    }
}

@WayspotMultiPreview
@Composable
private fun ProfileContentPreview() {
    WayspotTheme {
        val review = PreviewData.reviewDetailReview
        ProfileContent(
            user = PreviewData.userProfile,
            reviews = listOf(review),
            reviewFeedItems = listOf(
                ReviewFeedItem(
                    review = review,
                    place = PreviewDataPopular.previewPlaceInfo,
                    author = PreviewData.reviewDetailAuthor
                )
            ),
            onEditProfileClick = {},
            onSavedPlacesClick = {},
            onSignOutClick = {},
            onReviewClick = {}
        )
    }
}

