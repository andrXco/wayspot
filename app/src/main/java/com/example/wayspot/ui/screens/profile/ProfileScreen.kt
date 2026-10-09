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

/** Sincroniza el perfil recibido con el ViewModel y conecta sus acciones de navegación. */
@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel,
    userProfile: UserProfile,
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier,
    onEditProfileClick: () -> Unit = {},
    onSavedPlacesClick: () -> Unit = {},
    onReviewClick: (String) -> Unit = {}
) {
    val state by profileViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        profileViewModel.loadProfile(userProfile)
        profileViewModel.loadReviews("1")
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
            modifier = modifier
        )
    }
}

@WayspotMultiPreview
@Composable
private fun ProfileScreenPreview() {
    WayspotTheme {
        ProfileContent(
            user = PreviewData.userProfile,
            reviews = emptyList(),
            onEditProfileClick = {},
            onSavedPlacesClick = {},
            onSignOutClick = {},
            onReviewClick = {}
        )
    }
}

