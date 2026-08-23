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
import com.example.wayspot.data.model.UserProfile
import com.example.wayspot.navigation.Routes
import com.example.wayspot.data.local.PreviewData
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.components.WayspotBottomBar
import com.example.wayspot.ui.components.WayspotHeader
import com.example.wayspot.ui.components.WayspotSearch
import com.example.wayspot.ui.screens.home.components.PostCard
import com.example.wayspot.ui.theme.WayspotTheme

@Composable
fun HomeScreen(
    onPlaceClick: (Place) -> Unit,
    currentRoute: String,
    onNavItemClick: (String) -> Unit,
    onNotificationsClick: () -> Unit,
    userProfile: UserProfile,
    onEditProfileClick: () -> Unit,
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
        onSearchChange = {
            searchText = it
        },
        onNotificationsClick = onNotificationsClick,
        onNavItemClick = onNavItemClick,
        onPlaceClick = onPlaceClick,
        onEditProfileClick = onEditProfileClick,
        modifier = modifier
    )
}

@Composable
fun HomeContent(
    posts: List<Post>,
    searchText: String,
    currentRoute: String,
    userProfile: UserProfile,
    onSearchChange: (String) -> Unit,
    onNotificationsClick: () -> Unit,
    onNavItemClick: (String) -> Unit,
    onPlaceClick: (Place) -> Unit,
    onEditProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {

        if (currentRoute != Routes.PROFILE) {
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
                        m = Modifier.fillMaxSize()
                    )
                }

                Routes.PROFILE -> {
                    com.example.wayspot.ui.screens.profile.ProfileScreen(
                        userProfile = userProfile,
                        onEditProfileClick = onEditProfileClick,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        WayspotBottomBar(
            currentRoute = currentRoute,
            onNavItemClick = onNavItemClick
        )
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
            onEditProfileClick = {}
        )
    }
}
