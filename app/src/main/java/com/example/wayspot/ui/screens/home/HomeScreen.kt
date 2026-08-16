package com.example.wayspot.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.wayspot.navigation.Routes
import com.example.wayspot.ui.preview.PreviewData
import com.example.wayspot.ui.screens.home.components.HomeBottomBar
import com.example.wayspot.ui.screens.home.components.HomeHeader
import com.example.wayspot.ui.screens.home.components.HomeSearch
import com.example.wayspot.ui.screens.home.components.PostCard
import com.example.wayspot.ui.theme.WayspotTheme

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    var currentTab by remember { mutableStateOf(Routes.HOME) }
    
    HomeContent(
        posts = PreviewData.listPosts,
        searchText = "",
        currentRoute = currentTab,
        onSearchChange = { /* Lógica de búsqueda */ },
        onNotificationsClick = { /* Lógica de notificaciones */ },
        onNavItemClick = { currentTab = it },
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
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            HomeHeader(
                onNotificationsClick = onNotificationsClick,
                modifier = Modifier.padding(16.dp)
            )
        },
        bottomBar = {
            HomeBottomBar(
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
                        HomeSearch(
                            searchText = searchText,
                            onSearchChange = onSearchChange
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        PostList(posts = posts)
                    }
                }
                Routes.EXPLORE -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = androidx.compose.ui.Alignment.Center
                    ) {
                        androidx.compose.material3.Text(text = "Explorar contenido")
                    }
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
private fun PostList(posts: List<com.example.wayspot.model.Post>, modifier: Modifier = Modifier) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        items(posts) { post ->
            PostCard(post = post)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    WayspotTheme {
        HomeContent(
            posts = PreviewData.listPosts,
            searchText = "",
            currentRoute = Routes.HOME,
            onSearchChange = {},
            onNotificationsClick = {},
            onNavItemClick = {}
        )
    }
}
