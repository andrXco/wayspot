package com.example.wayspot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.wayspot.navigation.AppNavigation
import com.example.wayspot.ui.theme.WayspotTheme
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.wayspot.navigation.Routes
import com.example.wayspot.ui.components.WayspotBottomBar
import androidx.compose.runtime.getValue
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            WayspotTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    contentWindowInsets = WindowInsets.safeDrawing,

                    bottomBar = {
                        if (
                            currentRoute == Routes.HOME ||
                            currentRoute == Routes.EXPLORE ||
                            currentRoute == Routes.PROFILE
                        ) {
                            WayspotBottomBar(
                                currentRoute = currentRoute ?: Routes.HOME,
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
                        AppNavigation(
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}
