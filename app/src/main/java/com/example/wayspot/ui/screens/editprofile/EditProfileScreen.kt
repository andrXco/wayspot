package com.example.wayspot.ui.screens.editprofile

import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.wayspot.data.local.PreviewData
import com.example.wayspot.data.model.ProfileNotificationPreferences
import com.example.wayspot.data.model.UserProfile
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.screens.editprofile.components.EditProfileAvatarSection
import com.example.wayspot.ui.screens.editprofile.components.EditProfileBottomAction
import com.example.wayspot.ui.screens.editprofile.components.EditProfileDangerZone
import com.example.wayspot.ui.screens.editprofile.components.EditProfileDeleteConfirmationDialog
import com.example.wayspot.ui.screens.editprofile.components.EditProfileFormSection
import com.example.wayspot.ui.screens.editprofile.components.EditProfileHeader
import com.example.wayspot.ui.screens.editprofile.components.EditProfileNotificationsSection
import com.example.wayspot.ui.theme.WayspotTheme

@Composable
fun EditProfileScreen(
    editProfileViewModel: EditProfileViewModel,
    profile: UserProfile,
    onBackClick: () -> Unit,
    onSaveClick: (UserProfile) -> Unit,
    onDeleteAccountConfirmed: () -> Unit,
    modifier: Modifier = Modifier
) {

    val state by editProfileViewModel.uiState.collectAsState()

    LaunchedEffect(profile) {
        editProfileViewModel.loadProfile(profile)
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { selectedUri ->
        if (selectedUri != null) {
            editProfileViewModel.updateAvatarUrl(
                selectedUri.toString()
            )
        }
    }

    BackHandler {
        if (state.isDeleteConfirmationVisible) {
            editProfileViewModel.hideDeleteConfirmation()
        } else {
            onBackClick()
        }
    }

    EditProfileContent(
        initials = state.initials,
        avatarUrl = state.avatarUrl,
        username = state.username,
        onUsernameChange = {
            editProfileViewModel.updateUsername(it)
        },
        email = state.email,
        onEmailChange = {
            editProfileViewModel.updateEmail(it)
        },
        bio = state.bio,
        onBioChange = {
            editProfileViewModel.updateBio(it)
        },
        location = state.location,
        onLocationChange = {
            editProfileViewModel.updateLocation(it)
        },
        notificationPreferences = state.notificationPreferences,
        onNewFollowersChange = {
            editProfileViewModel.updateNewFollowersEnabled(it)
        },
        onReviewCommentsChange = {
            editProfileViewModel.updateReviewCommentsEnabled(it)
        },
        onReceivedLikesChange = {
            editProfileViewModel.updateReceivedLikesEnabled(it)
        },
        isSaveEnabled = state.isSaveEnabled,
        onBackClick = onBackClick,
        onChangePhotoClick = {
            photoPickerLauncher.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
        },
        onDeleteAccountClick = {
            editProfileViewModel.showDeleteConfirmation()
        },
        onSaveClick = {
            editProfileViewModel.profileForSaving()?.let(onSaveClick)
        },
        isDeleteConfirmationVisible = state.isDeleteConfirmationVisible,
        onDeleteConfirmationDismiss = {
            editProfileViewModel.hideDeleteConfirmation()
        },
        onDeleteAccountConfirmed = {
            editProfileViewModel.hideDeleteConfirmation()
            onDeleteAccountConfirmed()
        },
        modifier = modifier
    )
}

@Composable
fun EditProfileContent(
    initials: String,
    avatarUrl: String?,
    username: String,
    onUsernameChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    bio: String,
    onBioChange: (String) -> Unit,
    location: String,
    onLocationChange: (String) -> Unit,
    notificationPreferences: ProfileNotificationPreferences,
    onNewFollowersChange: (Boolean) -> Unit,
    onReviewCommentsChange: (Boolean) -> Unit,
    onReceivedLikesChange: (Boolean) -> Unit,
    isSaveEnabled: Boolean,
    onBackClick: () -> Unit,
    onChangePhotoClick: () -> Unit,
    onDeleteAccountClick: () -> Unit,
    onSaveClick: () -> Unit,
    isDeleteConfirmationVisible: Boolean,
    onDeleteConfirmationDismiss: () -> Unit,
    onDeleteAccountConfirmed: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .imePadding()
        ) {
            EditProfileHeader(
                onBackClick = onBackClick,
                modifier = Modifier.fillMaxWidth()
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item(key = "avatar") {
                    EditProfileAvatarSection(
                        avatarUrl = avatarUrl,
                        initials = initials,
                        onChangePhotoClick = onChangePhotoClick
                    )
                }

                item(key = "form") {
                    EditProfileFormSection(
                        username = username,
                        onUsernameChange = onUsernameChange,
                        email = email,
                        onEmailChange = onEmailChange,
                        bio = bio,
                        onBioChange = onBioChange,
                        location = location,
                        onLocationChange = onLocationChange,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }

                item(key = "notifications") {
                    EditProfileNotificationsSection(
                        newFollowersEnabled = notificationPreferences.newFollowers,
                        onNewFollowersChange = onNewFollowersChange,
                        reviewCommentsEnabled = notificationPreferences.reviewComments,
                        onReviewCommentsChange = onReviewCommentsChange,
                        receivedLikesEnabled = notificationPreferences.likesReceived,
                        onReceivedLikesChange = onReceivedLikesChange,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }

                item(key = "danger_zone") {
                    EditProfileDangerZone(
                        onDeleteAccountClick = onDeleteAccountClick,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }

            EditProfileBottomAction(
                isEnabled = isSaveEnabled,
                onSaveClick = onSaveClick,
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (isDeleteConfirmationVisible) {
            EditProfileDeleteConfirmationDialog(
                onDismissRequest = onDeleteConfirmationDismiss,
                onConfirmClick = onDeleteAccountConfirmed
            )
        }
    }
}

@WayspotMultiPreview
@Composable
private fun EditProfileScreenPreview() {
    val profile = PreviewData.userProfile

    WayspotTheme {
        EditProfileContent(
            initials = profile.initials,
            avatarUrl = profile.avatarUrl,
            username = profile.username,
            onUsernameChange = {},
            email = profile.email,
            onEmailChange = {},
            bio = profile.bio,
            onBioChange = {},
            location = profile.location,
            onLocationChange = {},
            notificationPreferences = profile.notificationPreferences,
            onNewFollowersChange = {},
            onReviewCommentsChange = {},
            onReceivedLikesChange = {},
            isSaveEnabled = true,
            onBackClick = {},
            onChangePhotoClick = {},
            onDeleteAccountClick = {},
            onSaveClick = {},
            isDeleteConfirmationVisible = false,
            onDeleteConfirmationDismiss = {},
            onDeleteAccountConfirmed = {}
        )
    }
}
