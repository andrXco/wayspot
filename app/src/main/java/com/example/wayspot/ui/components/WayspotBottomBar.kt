package com.example.wayspot.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.wayspot.R
import com.example.wayspot.navigation.Screen
import com.example.wayspot.ui.theme.WayspotTheme

data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val labelRes: Int
)

@Composable
fun WayspotBottomBar(
    currentRoute: String,
    onNavItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        BottomNavItem(
            Screen.Explore.route,
            Icons.Default.Search,
            R.string.nav_explore
        ),
        BottomNavItem(
            Screen.Home.route,
            Icons.Default.Home,
            R.string.nav_home
        ),
        BottomNavItem(
            Screen.Profile.route,
            Icons.Default.Person,
            R.string.nav_profile
        )
    )

    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {

        NavigationBarItem(
            icon = {
                Icon(
                    items[0].icon,
                    contentDescription = null
                )
            },
            label = {
                Text(stringResource(items[0].labelRes))
            },
            selected = currentRoute == items[0].route,
            onClick = {
                onNavItemClick(items[0].route)
            }
        )

        NavigationBarItem(
            icon = {
                Icon(
                    items[1].icon,
                    contentDescription = null
                )
            },
            label = {
                Text(stringResource(items[1].labelRes))
            },
            selected = currentRoute == items[1].route,
            onClick = {
                onNavItemClick(items[1].route)
            }
        )

        NavigationBarItem(
            icon = {
                Icon(
                    items[2].icon,
                    contentDescription = null
                )
            },
            label = {
                Text(stringResource(items[2].labelRes))
            },
            selected = currentRoute == items[2].route,
            onClick = {
                onNavItemClick(items[2].route)
            }
        )
    }
}

@Preview
@Composable
private fun WayspotBottomBarPreview() {
    WayspotTheme {
        WayspotBottomBar(
            currentRoute = Screen.Home.route,
            onNavItemClick = {}
        )
    }
}