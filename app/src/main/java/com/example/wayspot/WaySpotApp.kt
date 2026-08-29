package com.example.wayspot

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
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
        val drawsBehindStatusBar = currentRoute in edgeToEdgeRoutes
        val useDarkStatusBarIcons = when (currentRoute) {
            Screen.Splash.route -> false
            Screen.Profile.route -> MaterialTheme.colorScheme.primary.luminance() > 0.5f
            else -> MaterialTheme.colorScheme.background.luminance() > 0.5f
        }
        val view = LocalView.current

        SideEffect {
            view.context.findActivity()?.let { activity ->
                WindowCompat.getInsetsController(
                    activity.window,
                    view
                ).isAppearanceLightStatusBars = useDarkStatusBarIcons
            }
        }

        Scaffold(
            modifier = modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets.safeDrawing.only(
                WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom
            ),
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
                AppNavigation(
                    navController = navController,
                    modifier = if (drawsBehindStatusBar) {
                        Modifier
                    } else {
                        Modifier.statusBarsPadding()
                    }
                )
            }
        }
    }
}

private val bottomBarRoutes = setOf(
    Screen.Home.route,
    Screen.Explore.route,
    Screen.Profile.route
)

private val edgeToEdgeRoutes = setOf(
    Screen.Splash.route,
    Screen.Profile.route
)

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
