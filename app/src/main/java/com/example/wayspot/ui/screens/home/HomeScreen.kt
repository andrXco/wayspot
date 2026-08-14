package com.example.wayspot.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.wayspot.ui.preview.PreviewData
import com.example.wayspot.ui.screens.home.components.HomeTopSection
import com.example.wayspot.ui.screens.home.components.PostCard
import com.example.wayspot.ui.theme.WayspotTheme

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        HomeTopSection(
            searchText = "",
            onSearchChange = { /* Lógica de búsqueda */ },
            onNotificationsClick = { /* Lógica de notificaciones */ }
        )

        Spacer(modifier = Modifier.height(16.dp))

        PostList(posts = PreviewData.listPosts)
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
        HomeScreen()
    }
}
