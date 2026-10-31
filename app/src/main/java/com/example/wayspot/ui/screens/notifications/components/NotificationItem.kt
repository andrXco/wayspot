package com.example.wayspot.ui.screens.notifications.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.wayspot.R
import com.example.wayspot.data.local.PreviewData
import com.example.wayspot.data.model.notification.Notification
import com.example.wayspot.data.model.notification.NotificationType
import com.example.wayspot.ui.components.ProfileAvatar
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.theme.WayspotTheme

/** Representa una notificación con identidad visual, mensaje enriquecido y metadatos contextuales. */
@Composable
fun NotificationItem(
    notification: Notification,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 12.dp
            ),
        verticalAlignment = Alignment.Top
    ) {

        ProfileAvatar(
            avatarUrl = notification.avatarUrl,
            initials = notification.username.split(" ").take(2).mapNotNull { it.firstOrNull()?.uppercase() }.joinToString(""),
            contentDescription = stringResource(R.string.notifications_actor_avatar_description, notification.username),
            modifier = Modifier.size(40.dp)
        )

        Spacer(
            modifier = Modifier.width(12.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = buildAnnotatedString {

                    withStyle(
                        SpanStyle(
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        append(notification.username)
                    }

                    append(" ")

                    withStyle(
                        SpanStyle(
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        append(
                            stringResource(
                                if (notification.type == NotificationType.FOLLOW) {
                                    R.string.notifications_follow_message
                                } else {
                                    R.string.notifications_like_message
                                }
                            )
                        )
                    }
                },
                style = MaterialTheme.typography.bodySmall
            )

            if (notification.detail.isNotBlank()) {
                Text(
                    text = notification.detail,
                    modifier = Modifier.padding(top = 3.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = android.text.format.DateUtils.getRelativeTimeSpanString(
                    notification.timestampMillis,
                    System.currentTimeMillis(),
                    android.text.format.DateUtils.MINUTE_IN_MILLIS,
                    android.text.format.DateUtils.FORMAT_ABBREV_RELATIVE
                ).toString(),
                modifier = Modifier.padding(top = 3.dp),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@WayspotMultiPreview
@Composable
private fun NotificationItemPreview() {
    WayspotTheme {
        NotificationItem(
            notification = PreviewData.notifications.first()
        )
    }
}
