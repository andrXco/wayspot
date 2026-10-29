package com.example.wayspot.ui.screens.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.wayspot.data.local.PreviewData
import com.example.wayspot.data.local.PreviewDataPopular
import com.example.wayspot.data.model.review.ReviewFeedItem
import com.example.wayspot.data.model.user.UserProfile
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.screens.profile.components.ProfileContent
import com.example.wayspot.ui.theme.WayspotTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

/** Sincroniza el perfil recibido con el ViewModel y conecta sus acciones de navegación. */
@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel,
    userProfile: UserProfile,
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier,
    onEditProfileClick: () -> Unit = {},
    onSavedPlacesClick: () -> Unit = {},
    onReviewClick: (String) -> Unit = {},
    onReviewCommentClick: (String) -> Unit = {},
    onReviewPlaceClick: (String) -> Unit = {}
) {
    val state by profileViewModel.uiState.collectAsState()

    LaunchedEffect(userProfile) {
        profileViewModel.loadProfile(userProfile)
    }

    LaunchedEffect(Unit) {
        profileViewModel.loadReviews()
    }



    state.userProfile?.let { profile ->
        ProfileContent(
            user = profile,
            reviews = state.reviews,
            isLoading = state.isLoading,
            errorMessage = state.errorMessage,
            onEditProfileClick = onEditProfileClick,
            onSavedPlacesClick = onSavedPlacesClick,
            onSignOutClick = {
                profileViewModel.signOut()
                onSignOut()
            },
            onReviewClick = onReviewClick,
            reviewFeedItems = state.reviewFeedItems,
            reviewLikeCounts = state.reviewLikeCounts,
            likedReviewIds = state.likedReviewIds,
            expandedReviewIds = state.expandedReviewIds,
            sharedReviewIds = state.sharedReviewIds,
            onReviewLikeClick = profileViewModel::toggleReviewLike,
            onReviewExpandClick = profileViewModel::toggleReviewExpanded,
            onReviewCommentClick = onReviewCommentClick,
            onReviewShareClick = profileViewModel::toggleReviewShared,
            onReviewPlaceClick = onReviewPlaceClick,
            modifier = modifier
        )
    }
}

@WayspotMultiPreview
@Composable
private fun ProfileScreenPreview() {
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

