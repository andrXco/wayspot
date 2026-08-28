package com.example.wayspot.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.wayspot.data.local.PreviewData
import com.example.wayspot.data.local.PreviewDataPopular
import com.example.wayspot.data.model.Place
import com.example.wayspot.data.model.Post
import com.example.wayspot.ui.components.WayspotHeader
import com.example.wayspot.ui.components.WayspotSearch
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.screens.home.components.PostCard
import com.example.wayspot.ui.theme.WayspotTheme

@Composable
fun HomeScreen(
    onPlaceClick: (Place) -> Unit,
    onNotificationsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchText by remember {
        mutableStateOf("")
    }

    HomeContent(
        posts = PreviewData.listPosts,
        searchText = searchText,
        onSearchChange = {
            searchText = it
        },
        onNotificationsClick = onNotificationsClick,
        onPlaceClick = onPlaceClick,
        modifier = modifier
    )
}

@Composable
fun HomeContent(
    posts: List<Post>,
    searchText: String,
    onSearchChange: (String) -> Unit,
    onNotificationsClick: () -> Unit,
    onPlaceClick: (Place) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        WayspotHeader(
            onNotificationsClick = onNotificationsClick,
            modifier = Modifier.padding(vertical = 16.dp)
        )

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
            onNotificationsClick = {},
            onPlaceClick = {}
        )
    }
}