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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.wayspot.data.local.PreviewData
import com.example.wayspot.data.model.Post
import com.example.wayspot.ui.components.WayspotHeader
import com.example.wayspot.ui.components.WayspotSearch
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.screens.home.components.PostCard
import com.example.wayspot.ui.theme.WayspotTheme

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    onPlaceClick: (String) -> Unit,
    onNotificationsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by homeViewModel.uiState.collectAsState()

    HomeContent(
        posts = state.posts,
        searchText = state.searchText,
        onSearchChange = {
            homeViewModel.updateSearchText(it)
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
    onPlaceClick: (String) -> Unit,
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
    onPlaceClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        items(
            items = posts,
            key = { post ->
                "${post.usuario}|${post.tiempo}|${post.placeId.orEmpty()}"
            }
        ) { post ->
            PostCard(
                post = post,
                onClick = {
                    if (post.placeId != null) {
                        onPlaceClick(post.placeId)
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
        HomeContent(
            posts = PreviewData.listPosts,
            searchText = "",
            onSearchChange = {},
            onNotificationsClick = {},
            onPlaceClick = {}
        )
    }
}
