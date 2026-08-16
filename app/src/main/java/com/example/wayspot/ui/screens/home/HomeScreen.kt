package com.example.wayspot.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.wayspot.navigation.Routes
import com.example.wayspot.ui.preview.PreviewData
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.components.WayspotBottomBar
import com.example.wayspot.ui.components.WayspotHeader
import com.example.wayspot.ui.components.WayspotSearch
import com.example.wayspot.ui.screens.home.components.PostCard
import com.example.wayspot.ui.theme.WayspotTheme

@Composable
fun HomeScreen(
    onPlaceClick: (com.example.wayspot.model.Places) -> Unit,
    currentRoute: String,
    onNavItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    HomeContent(
        posts = PreviewData.listPosts,
        searchText = "",
        currentRoute = currentRoute,
        onSearchChange = { /* Lógica de búsqueda */ },
        onNotificationsClick = { /* Lógica de notificaciones */ },
        onNavItemClick = onNavItemClick,
        onPlaceClick = onPlaceClick,
        modifier = modifier
    )
}

@Composable
fun HomeContent(
    posts: List<com.example.wayspot.model.Post>,
    searchText: String,
    currentRoute: String,
    onSearchChange: (String) -> Unit,
    onNotificationsClick: () -> Unit,
    onNavItemClick: (String) -> Unit,
    onPlaceClick: (com.example.wayspot.model.Places) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            WayspotHeader(
                onNotificationsClick = onNotificationsClick,
                modifier = Modifier.padding(16.dp)
            )
        },
        bottomBar = {
            WayspotBottomBar(
                currentRoute = currentRoute,
                onNavItemClick = onNavItemClick
            )
        },
        modifier = modifier
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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

                        Spacer(modifier = Modifier.height(16.dp))

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
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = androidx.compose.ui.Alignment.Center
                    ) {
                        androidx.compose.material3.Text(text = "Perfil de usuario")
                    }
                }
            }
        }
    }
}

@Composable
private fun PostList(
    posts: List<com.example.wayspot.model.Post>,
    onPlaceClick: (com.example.wayspot.model.Places) -> Unit,
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
                    // Buscar el lugar correspondiente por placeId
                    val place = com.example.wayspot.ui.preview.PreviewDataPopular.listPlaces.find { 
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
        HomeContent(
            posts = PreviewData.listPosts,
            searchText = "",
            currentRoute = Routes.HOME,
            onSearchChange = {},
            onNotificationsClick = {},
            onNavItemClick = {},
            onPlaceClick = {}
        )
    }
}
