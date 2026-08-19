package com.example.wayspot.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.wayspot.ui.components.WayspotBottomBar
import com.example.wayspot.ui.components.WayspotHeader

@Composable
fun AppScaffold(
    currentRoute: String,
    homeRoute: String,
    onNavItemClick: (String) -> Unit,
    onNotificationsClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (Modifier) -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            if (currentRoute == Routes.HOME) {
                WayspotHeader(
                    onNotificationsClick = onNotificationsClick
                )
            }
        },
        bottomBar = {
            if (currentRoute == Routes.HOME) {
                WayspotBottomBar(
                    currentRoute = homeRoute,
                    onNavItemClick = onNavItemClick
                )
            }
        }
    ) { innerPadding ->

        content(
            Modifier.padding(innerPadding)
        )
    }
}