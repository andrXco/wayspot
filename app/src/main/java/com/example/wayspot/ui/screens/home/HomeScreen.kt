package com.example.wayspot.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.wayspot.model.Places
import com.example.wayspot.model.Post
import com.example.wayspot.navigation.Routes
import com.example.wayspot.ui.components.WayspotSearch
import com.example.wayspot.ui.preview.PreviewData
import com.example.wayspot.ui.preview.PreviewDataPopular
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.screens.explore.ExploreScreen
import com.example.wayspot.ui.screens.home.components.PostCard
import com.example.wayspot.ui.theme.WayspotTheme
import com.example.wayspot.ui.screens.profile.ProfileScreen

@Composable
fun HomeScreen(
    onPlaceClick: (Places) -> Unit,
    currentRoute: String,
    modifier: Modifier = Modifier
) {
    var searchText by remember {
        mutableStateOf("")
    }

    HomeContent(
        posts = PreviewData.listPosts,
        searchText = searchText,
        currentRoute = currentRoute,
        onSearchChange = {
            searchText = it
        },
        onPlaceClick = onPlaceClick,
        modifier = modifier
    )
}

@Composable
fun HomeContent(
    posts: List<Post>,
    searchText: String,
    currentRoute: String,
    onSearchChange: (String) -> Unit,
    onPlaceClick: (Places) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        when (currentRoute) {

            Routes.HOME -> {
                HomeFeed(
                    posts = posts,
                    searchText = searchText,
                    onSearchChange = onSearchChange,
                    onPlaceClick = onPlaceClick,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Routes.EXPLORE -> {
                ExploreScreen(
                    onPlaceClick = onPlaceClick,
                    m = Modifier.fillMaxSize()
                )
            }

            Routes.PROFILE -> {
                ProfileScreen(
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun HomeFeed(
    posts: List<Post>,
    searchText: String,
    onSearchChange: (String) -> Unit,
    onPlaceClick: (Places) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
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

@Composable
private fun PostList(
    posts: List<Post>,
    onPlaceClick: (Places) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(posts) { post ->

            PostCard(
                post = post,
                onClick = {
                    val place = PreviewDataPopular.listPlaces.find {
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
            onPlaceClick = {}
        )
    }
}