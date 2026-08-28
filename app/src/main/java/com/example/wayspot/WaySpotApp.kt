package com.example.wayspot

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.wayspot.navigation.AppNavigation
import com.example.wayspot.navigation.Screen
import com.example.wayspot.ui.components.WayspotBottomBar
import com.example.wayspot.ui.theme.WayspotTheme

@Composable
fun WaySpotApp(
    modifier: Modifier = Modifier
) {
    WayspotTheme {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        Scaffold(
            modifier = modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets.safeDrawing,
            bottomBar = {
                if (currentRoute != null && currentRoute in bottomBarRoutes) {
                    WayspotBottomBar(
                        currentRoute = currentRoute,
                        onNavItemClick = { route ->
                            navController.navigate(route)
                        }
                    )
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                AppNavigation(navController = navController)
            }
        }
    }
}

private val bottomBarRoutes = setOf(
    Screen.Home.route,
    Screen.Explore.route,
    Screen.Profile.route
)
