package com.example.wayspot.ui.screens.editprofile

import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.wayspot.R
import com.example.wayspot.data.local.PreviewData
import com.example.wayspot.data.model.EditProfileRules
import com.example.wayspot.data.model.ProfileNotificationPreferences
import com.example.wayspot.data.model.UserProfile
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.screens.editprofile.components.EditProfileAvatarSection
import com.example.wayspot.ui.screens.editprofile.components.EditProfileBottomAction
import com.example.wayspot.ui.screens.editprofile.components.EditProfileDangerZone
import com.example.wayspot.ui.screens.editprofile.components.EditProfileFormSection
import com.example.wayspot.ui.screens.editprofile.components.EditProfileHeader
import com.example.wayspot.ui.screens.editprofile.components.EditProfileNotificationsSection
import com.example.wayspot.ui.theme.WayspotTheme

@Composable
fun EditProfileScreen(
    profile: UserProfile,
    onBackClick: () -> Unit,
    onSaveClick: (UserProfile) -> Unit,
    onDeleteAccountConfirmed: () -> Unit,
    modifier: Modifier = Modifier
) {
    var username by rememberSaveable(profile.username) {
        mutableStateOf(profile.username)
    }
    var email by rememberSaveable(profile.email) {
        mutableStateOf(profile.email)
    }
    var bio by rememberSaveable(profile.bio) {
        mutableStateOf(profile.bio)
    }
    var location by rememberSaveable(profile.location) {
        mutableStateOf(profile.location)
    }
    var avatarUrl by rememberSaveable(profile.avatarUrl) {
        mutableStateOf(profile.avatarUrl)
    }
    var newFollowersEnabled by rememberSaveable(
        profile.notificationPreferences.newFollowers
    ) {
        mutableStateOf(profile.notificationPreferences.newFollowers)
    }
    var reviewCommentsEnabled by rememberSaveable(
        profile.notificationPreferences.reviewComments
    ) {
        mutableStateOf(profile.notificationPreferences.reviewComments)
    }
    var receivedLikesEnabled by rememberSaveable(
        profile.notificationPreferences.likesReceived
    ) {
        mutableStateOf(profile.notificationPreferences.likesReceived)
    }
    var isDeleteConfirmationVisible by rememberSaveable {
        mutableStateOf(false)
    }

    val isSaveEnabled by remember {
        derivedStateOf {
            EditProfileRules.canSave(
                username = username,
                email = email,
                location = location
            )
        }
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { selectedUri ->
        if (selectedUri != null) {
            avatarUrl = selectedUri.toString()
        }
    }

    BackHandler {
        if (isDeleteConfirmationVisible) {
            isDeleteConfirmationVisible = false
        } else {
            onBackClick()
        }
    }

    EditProfileContent(
        initials = profile.initials,
        avatarUrl = avatarUrl,
        username = username,
        onUsernameChange = { value ->
            username = EditProfileRules.filterUsername(value)
        },
        email = email,
        onEmailChange = { email = it },
        bio = bio,
        onBioChange = { value ->
            bio = value.take(EditProfileRules.MAX_BIO_LENGTH)
        },
        location = location,
        onLocationChange = { location = it },
        notificationPreferences = ProfileNotificationPreferences(
            newFollowers = newFollowersEnabled,
            reviewComments = reviewCommentsEnabled,
            likesReceived = receivedLikesEnabled
        ),
        onNewFollowersChange = { newFollowersEnabled = it },
        onReviewCommentsChange = { reviewCommentsEnabled = it },
        onReceivedLikesChange = { receivedLikesEnabled = it },
        isSaveEnabled = isSaveEnabled,
        onBackClick = onBackClick,
        onChangePhotoClick = {
            photoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        },
        onDeleteAccountClick = {
            isDeleteConfirmationVisible = true
        },
        onSaveClick = {
            if (isSaveEnabled) {
                onSaveClick(
                    EditProfileRules.normalizeProfile(
                        profile = profile,
                        username = username,
                        email = email,
                        bio = bio,
                        location = location,
                        avatarUrl = avatarUrl,
                        preferences = ProfileNotificationPreferences(
                            newFollowers = newFollowersEnabled,
                            reviewComments = reviewCommentsEnabled,
                            likesReceived = receivedLikesEnabled
                        )
                    )
                )
            }
        },
        modifier = modifier
    )

    if (isDeleteConfirmationVisible) {
        AlertDialog(
            onDismissRequest = {
                isDeleteConfirmationVisible = false
            },
            title = {
                Text(text = stringResource(R.string.edit_profile_delete_account))
            },
            text = {
                Text(text = stringResource(R.string.edit_profile_delete_confirmation))
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        isDeleteConfirmationVisible = false
                        onDeleteAccountConfirmed()
                    }
                ) {
                    Text(text = stringResource(R.string.edit_profile_delete_account))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        isDeleteConfirmationVisible = false
                    }
                ) {
                    Text(text = stringResource(R.string.edit_profile_cancel))
                }
            }
        )
    }
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
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
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
            isSaveEnabled = EditProfileRules.canSave(
                username = profile.username,
                email = profile.email,
                location = profile.location
            ),
            onBackClick = {},
            onChangePhotoClick = {},
            onDeleteAccountClick = {},
            onSaveClick = {}
        )
    }
}
