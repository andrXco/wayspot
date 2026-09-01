package com.example.wayspot.ui.screens.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.wayspot.data.local.PreviewData
import com.example.wayspot.data.model.UserProfile
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.screens.profile.components.ProfileContent
import com.example.wayspot.ui.theme.WayspotTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel,
    userProfile: UserProfile,
    modifier: Modifier = Modifier,
    onEditProfileClick: () -> Unit = {},
    onSavedPlacesClick: () -> Unit = {}
) {
    val state by profileViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        profileViewModel.loadProfile(userProfile)
    }

    ProfileContent(
        user = state.userProfile,
        reviews = PreviewData.listReviews,
        onEditProfileClick = onEditProfileClick,
        onSavedPlacesClick = onSavedPlacesClick,
        modifier = modifier
    )
}

@WayspotMultiPreview
@Composable
private fun ProfileScreenPreview() {
    WayspotTheme {
        ProfileContent(
            user = PreviewData.userProfile,
            reviews = PreviewData.listReviews,
            onEditProfileClick = {},
            onSavedPlacesClick = {}
        )
    }
}

