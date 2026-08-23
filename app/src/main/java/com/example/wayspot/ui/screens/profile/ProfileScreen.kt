package com.example.wayspot.ui.screens.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.wayspot.data.local.PreviewData
import com.example.wayspot.data.model.UserProfile
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.screens.profile.components.ProfileContent
import com.example.wayspot.ui.theme.WayspotTheme

@Composable
fun ProfileScreen(
    userProfile: UserProfile,
    modifier: Modifier = Modifier,
    onEditProfileClick: () -> Unit = {}
) {
    var selectedTabIndex by remember {
        mutableIntStateOf(0)
    }

    ProfileContent(
        user = userProfile,
        reviews = PreviewData.listReviews,
        selectedTabIndex = selectedTabIndex,
        onTabSelected = {
            selectedTabIndex = it
        },
        onEditProfileClick = onEditProfileClick,
        modifier = modifier
    )
}

@WayspotMultiPreview
@Composable
private fun ProfileScreenPreview() {
    WayspotTheme {
        ProfileScreen(userProfile = PreviewData.userProfile)
    }
}

