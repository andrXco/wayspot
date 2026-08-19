package com.example.wayspot.ui.screens.notifications

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.wayspot.model.NotificationInfo
import com.example.wayspot.ui.preview.PreviewData
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.screens.notifications.components.NotificationItem
import com.example.wayspot.ui.theme.WayspotTheme
import com.example.wayspot.ui.screens.notifications.components.NotificationsTitle

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
    notifications: List<NotificationInfo>,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(
            top = 16.dp,
            bottom = 24.dp
        )
    ) {
        item {
            NotificationsHeader(
                onBackClick = onBackClick
            )
        }

        items(notifications) { notification ->
            NotificationItem(
                notification = notification
            )
        }
    }
}

@Composable
private fun NotificationsHeader(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Volver"
            )
        }

        NotificationsTitle()
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