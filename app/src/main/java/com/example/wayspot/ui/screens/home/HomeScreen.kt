package com.example.wayspot.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.wayspot.data.model.Post
import com.example.wayspot.data.local.PreviewDataPopular
import com.example.wayspot.data.model.Place
import com.example.wayspot.data.model.SavedPlace
import com.example.wayspot.data.model.SavedPlaceList
import com.example.wayspot.data.model.UserProfile
import com.example.wayspot.navigation.Routes
import com.example.wayspot.data.local.PreviewData
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.components.WayspotBottomBar
import com.example.wayspot.ui.components.WayspotHeader
import com.example.wayspot.ui.components.WayspotSearch
import com.example.wayspot.ui.screens.home.components.PostCard
import com.example.wayspot.ui.screens.savedplaces.SavedPlacesScreen
import com.example.wayspot.ui.theme.WayspotTheme

@Composable
fun HomeScreen(
    onPlaceClick: (Place) -> Unit,
    currentRoute: String,
    onNavItemClick: (String) -> Unit,
    onNotificationsClick: () -> Unit,
    userProfile: UserProfile,
    onEditProfileClick: () -> Unit,
    savedPlaces: List<SavedPlace>,
    onSavedPlacesClick: () -> Unit,
    onRemoveSavedPlace: (String, SavedPlaceList) -> Unit,
    onToggleSavedPlace: (Place) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchText by remember {
        mutableStateOf("")
    }

    HomeContent(
        posts = PreviewData.listPosts,
        searchText = searchText,
        currentRoute = currentRoute,
        userProfile = userProfile,
        savedPlaces = savedPlaces,
        onSearchChange = {
            searchText = it
        },
        onNotificationsClick = onNotificationsClick,
        onNavItemClick = onNavItemClick,
        onPlaceClick = onPlaceClick,
        onEditProfileClick = onEditProfileClick,
        onSavedPlacesClick = onSavedPlacesClick,
        onRemoveSavedPlace = onRemoveSavedPlace,
        onToggleSavedPlace = onToggleSavedPlace,
        modifier = modifier
    )
}

@Composable
fun HomeContent(
    posts: List<Post>,
    searchText: String,
    currentRoute: String,
    userProfile: UserProfile,
    savedPlaces: List<SavedPlace>,
    onSearchChange: (String) -> Unit,
    onNotificationsClick: () -> Unit,
    onNavItemClick: (String) -> Unit,
    onPlaceClick: (Place) -> Unit,
    onEditProfileClick: () -> Unit,
    onSavedPlacesClick: () -> Unit,
    onRemoveSavedPlace: (String, SavedPlaceList) -> Unit,
    onToggleSavedPlace: (Place) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {

        if (currentRoute != Routes.PROFILE && currentRoute != Routes.SAVED_PLACES) {
            WayspotHeader(
                onNotificationsClick = onNotificationsClick,
                modifier = Modifier.padding(16.dp)
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when (currentRoute) {

                Routes.HOME -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    ) {
                        WayspotSearch(
                            searchText = searchText,
                            onSearchChange = onSearchChange
                        )

                        Spacer(
                            modifier = Modifier.height(16.dp)
                        )

                        PostList(
                            posts = posts,
                            onPlaceClick = onPlaceClick
                        )
                    }
                }

                Routes.EXPLORE -> {
                    com.example.wayspot.ui.screens.explore.ExploreScreen(
                        onPlaceClick = onPlaceClick,
                        savedPlaces = savedPlaces,
                        onSaveClick = onToggleSavedPlace,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Routes.PROFILE -> {
                    com.example.wayspot.ui.screens.profile.ProfileScreen(
                        userProfile = userProfile,
                        onEditProfileClick = onEditProfileClick,
                        onSavedPlacesClick = onSavedPlacesClick,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Routes.SAVED_PLACES -> {
                    SavedPlacesScreen(
                        savedPlaces = savedPlaces,
                        onBackClick = { onNavItemClick(Routes.PROFILE) },
                        onPlaceClick = onPlaceClick,
                        onRemoveFromList = onRemoveSavedPlace,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        if (currentRoute != Routes.SAVED_PLACES) {
            WayspotBottomBar(
                currentRoute = currentRoute,
                onNavItemClick = onNavItemClick
            )
        }
    }
}

@Composable
private fun PostList(
    posts: List<Post>,
    onPlaceClick: (Place) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        items(posts) { post ->

            PostCard(
                post = post,
                onClick = {

                    val place =
                        PreviewDataPopular
                            .listPlaces
                            .find {
                                it.id == post.placeId
                            }

                    if (place != null) {
                        onPlaceClick(place)
                    }
                }
            )
        }
    }
}

@WayspotMultiPreview
@Composable
private fun HomeScreenPreview() {
    WayspotTheme {
        HomeScreen(
            currentRoute = Routes.HOME,
            onNavItemClick = {},
            onNotificationsClick = {},
            onPlaceClick = {},
            userProfile = PreviewData.userProfile,
            onEditProfileClick = {},
            savedPlaces = PreviewData.savedPlaces,
            onSavedPlacesClick = {},
            onRemoveSavedPlace = { _, _ -> },
            onToggleSavedPlace = {}
        )
    }
}
