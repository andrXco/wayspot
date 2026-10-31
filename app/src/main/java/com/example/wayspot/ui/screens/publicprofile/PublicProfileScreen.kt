
package com.example.wayspot.ui.screens.publicprofile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.wayspot.R
import com.example.wayspot.data.local.PreviewData
import com.example.wayspot.data.local.PreviewDataPopular
import com.example.wayspot.data.model.user.ProfileNotificationPreferences
import com.example.wayspot.data.model.user.UserProfile
import com.example.wayspot.data.model.user.UserStats
import com.example.wayspot.data.model.review.ReviewFeedItem
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.screens.profile.components.ProfileContent
import com.example.wayspot.ui.theme.WayspotTheme

@Composable
fun PublicProfileScreen(
    viewModel: PublicProfileViewModel,
    userId: String,
    onBackClick: () -> Unit,
    onReviewClick: (String) -> Unit,
    onReviewCommentClick: (String) -> Unit,
    onReviewPlaceClick: (String) -> Unit,
    onFollowersClick: () -> Unit,
    onFollowingClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(userId) {
        viewModel.loadUser(userId)
    }

    PublicProfileContent(
        state = state,
        onBackClick = onBackClick,
        onReviewClick = onReviewClick,
        onReviewCommentClick = onReviewCommentClick,
        onReviewPlaceClick = onReviewPlaceClick,
        onFollowersClick = onFollowersClick,
        onFollowingClick = onFollowingClick,
        onFollowClick = viewModel::toggleFollow,
        onReviewLikeClick = viewModel::toggleReviewLike,
        onReviewExpandClick = viewModel::toggleReviewExpanded,
        modifier = modifier
    )
}

@Composable
fun PublicProfileContent(
    state: PublicProfileState,
    onBackClick: () -> Unit,
    onReviewClick: (String) -> Unit,
    onFollowersClick: () -> Unit,
    onFollowingClick: () -> Unit,
    onFollowClick: () -> Unit,
    onReviewCommentClick: (String) -> Unit,
    onReviewPlaceClick: (String) -> Unit,
    onReviewLikeClick: (String) -> Unit,
    onReviewExpandClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {

        val user = state.user
        val errorMessage = state.errorMessage?.takeIf(String::isNotBlank)
            ?: if (state.hasDataLoadError) {
                stringResource(R.string.public_profile_data_load_error)
            } else {
                null
            }

        if (user != null) {
            val userProfile = UserProfile(
                name = user.name,
                username = user.username,
                bio = user.bio.orEmpty(),
                email = "",
                location = user.location.orEmpty(),
                notificationPreferences = ProfileNotificationPreferences(
                    newFollowers = false,
                    reviewComments = false,
                    likesReceived = false
                ),
                avatarUrl = user.avatarUrl,
                initials = user.name.take(1).uppercase(),
                isVerified = false,
                stats = UserStats(
                    places = 0,
                    reviews = state.reviews.size,
                    followers = state.followersCount.toString(),
                    following = state.followingCount.toString()
                )
            )

            ProfileContent(
                user = userProfile,
                reviews = state.reviews,
                onEditProfileClick = {},
                onSavedPlacesClick = {},
                onSignOutClick = {},
                onReviewClick = onReviewClick,
                onFollowersClick = onFollowersClick,
                onFollowingClick = onFollowingClick,
                isOwnProfile = false,
                isLoading = state.isLoading,
                errorMessage = errorMessage,
                onBackClick = onBackClick,
                reviewFeedItems = state.reviewFeedItems,
                reviewLikeCounts = state.reviewLikeCounts,
                likedReviewIds = state.likedReviewIds,
                areReviewLikesLoaded = state.areReviewLikesLoaded,
                expandedReviewIds = state.expandedReviewIds,
                canFollow = state.canFollow,
                isFollowing = state.isFollowing,
                isFollowActionInProgress = state.isFollowActionInProgress,
                hasFollowError = state.hasFollowError,
                onFollowClick = onFollowClick,
                onReviewLikeClick = onReviewLikeClick,
                onReviewExpandClick = onReviewExpandClick,
                onReviewCommentClick = onReviewCommentClick,
                onReviewPlaceClick = onReviewPlaceClick,
                modifier = Modifier.weight(1f)
            )
        } else if (state.isLoading) {
            CircularProgressIndicator()
        } else {
            errorMessage?.let { message ->
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@WayspotMultiPreview
@Composable
private fun PublicProfileScreenPreview() {
    WayspotTheme {
        PublicProfileContent(
            state = PublicProfileState(
                user = PreviewData.reviewDetailAuthor,
                reviews = listOf(PreviewData.reviewDetailReview),
                reviewFeedItems = listOf(
                    ReviewFeedItem(
                        review = PreviewData.reviewDetailReview,
                        place = PreviewDataPopular.previewPlaceInfo,
                        author = PreviewData.reviewDetailAuthor
                    )
                ),
                reviewLikeCounts = mapOf(
                    PreviewData.reviewDetailReview.id to PreviewData.reviewDetailReview.likesCount
                ),
                areReviewLikesLoaded = true,
                canFollow = true,
                followersCount = 128,
                followingCount = 42
            ),
            onBackClick = {},
            onReviewClick = {},
            onReviewCommentClick = {},
            onReviewPlaceClick = {},
            onFollowersClick = {},
            onFollowingClick = {},
            onFollowClick = {},
            onReviewLikeClick = {},
            onReviewExpandClick = {}
        )
    }
}
