package com.example.wayspot.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.example.wayspot.R
import com.example.wayspot.navigation.Screen
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.theme.WayspotTheme

/** Describe un destino disponible en la barra de navegación principal. */
data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val labelRes: Int
)

/** Barra de navegación global que refleja la ruta activa y delega el cambio de destino. */
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
            Screen.Notifications.route,
            Icons.Default.Notifications,
            R.string.notifications_content_description
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
        WayspotNavigationBarItem(
            item = items[0],
            selected = currentRoute == items[0].route,
            onClick = { onNavItemClick(items[0].route) }
        )

        WayspotNavigationBarItem(
            item = items[1],
            selected = currentRoute == items[1].route,
            onClick = { onNavItemClick(items[1].route) }
        )

        WayspotNavigationBarItem(
            item = items[2],
            selected = currentRoute == items[2].route,
            onClick = { onNavItemClick(items[2].route) }
        )

        WayspotNavigationBarItem(
            item = items[3],
            selected = currentRoute == items[3].route,
            onClick = { onNavItemClick(items[3].route) }
        )
    }
}

/** Acción global para iniciar una reseña, ubicada fuera de la barra inferior. */
@Composable
fun WayspotAddReviewButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        shape = CircleShape,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(R.string.nav_add_review)
        )
    }
}

@Composable
private fun RowScope.WayspotNavigationBarItem(
    item: BottomNavItem,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = {
            Icon(
                imageVector = item.icon,
                contentDescription = stringResource(item.labelRes)
            )
        },
        modifier = modifier.weight(1f)
    )
}

@WayspotMultiPreview
@Composable
private fun WayspotBottomBarPreview() {
    WayspotTheme {
        WayspotBottomBar(
            currentRoute = Screen.Home.route,
            onNavItemClick = {}
        )
    }
}
