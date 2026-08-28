package com.example.wayspot.ui.screens.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.wayspot.data.local.PreviewData
import com.example.wayspot.data.model.Notification
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.screens.notifications.components.NotificationItem
import com.example.wayspot.ui.screens.notifications.components.NotificationsHeader
import com.example.wayspot.ui.theme.WayspotTheme

@Composable
fun NotificationsScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    NotificationsContent(
        notifications = PreviewData.notifications,
        onBackClick = onBackClick,
        modifier = modifier
    )
}

@Composable
fun NotificationsContent(
    notifications: List<Notification>,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        NotificationsHeader(
            onBackClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        )

        HorizontalDivider(
            color = MaterialTheme.colorScheme.outlineVariant
        )

        if (notifications.isEmpty()) {

            NotificationsEmptySection(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

        } else {

            val todayNotifications = notifications.take(4)
            val previousNotifications = notifications.drop(4)

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {

                item {
                    NotificationSectionTitle(
                        title = "HOY"
                    )
                }

                items(
                    count = todayNotifications.size,
                    key = { index ->
                        todayNotifications[index].id
                    }
                ) { index ->

                    NotificationItem(
                        notification = todayNotifications[index]
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(start = 72.dp),
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }

                if (previousNotifications.isNotEmpty()) {

                    item {
                        NotificationSectionTitle(
                            title = "ANTERIORES"
                        )
                    }

                    items(
                        count = previousNotifications.size,
                        key = { index ->
                            previousNotifications[index].id
                        }
                    ) { index ->

                        NotificationItem(
                            notification = previousNotifications[index]
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(start = 72.dp),
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NotificationSectionTitle(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 16.dp
            ),
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun NotificationsEmptySection(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Icon(
            imageVector = Icons.Outlined.NotificationsNone,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "No tienes notificaciones",
            modifier = Modifier.padding(top = 16.dp),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Cuando tengas nuevas notificaciones aparecerán aquí.",
            modifier = Modifier.padding(
                start = 32.dp,
                end = 32.dp,
                top = 8.dp
            ),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@WayspotMultiPreview
@Composable
private fun NotificationsScreenPreview() {
    WayspotTheme {
        NotificationsScreen(
            onBackClick = {}
        )
    }
}