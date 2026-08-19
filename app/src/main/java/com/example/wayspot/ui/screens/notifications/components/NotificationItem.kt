package com.example.wayspot.ui.screens.notifications.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.wayspot.model.NotificationInfo
import com.example.wayspot.ui.theme.WayspotTheme

@Composable
fun NotificationItem(
    notification: NotificationInfo,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        NotificationAvatar(
            username = notification.username
        )

        Spacer(
            modifier = Modifier.size(12.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "${notification.username} ${notification.message}",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = notification.time,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun NotificationAvatar(
    username: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = username.take(1),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NotificationPreview() {
    WayspotTheme {
        NotificationItem(
            notification = NotificationInfo(
                id = 1,
                username = "John Doe",
                message = "liked your photo",
                time = "2 minutes ago"
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}