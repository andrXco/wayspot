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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.wayspot.R
import com.example.wayspot.data.local.PreviewData
import com.example.wayspot.data.model.notification.Notification
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.screens.notifications.components.NotificationItem
import com.example.wayspot.ui.screens.notifications.components.NotificationsHeader
import com.example.wayspot.ui.theme.WayspotTheme

/** Observa las notificaciones y delega su representación al contenido sin estado. */
@Composable
fun NotificationsScreen(
    notificationsViewModel: NotificationsViewModel,
    modifier: Modifier = Modifier
) {
    val state by notificationsViewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, notificationsViewModel) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) notificationsViewModel.loadNotifications()
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    NotificationsContent(
        notifications = state.notifications,
        isLoading = state.isLoading,
        hasLoadError = state.hasLoadError,
        onRetry = notificationsViewModel::loadNotifications,
        modifier = modifier
    )
}

/** Agrupa las notificaciones disponibles y presenta un estado vacío cuando corresponde. */
@Composable
fun NotificationsContent(
    notifications: List<Notification>,
    isLoading: Boolean = false,
    hasLoadError: Boolean = false,
    onRetry: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        NotificationsHeader(
            modifier = Modifier.fillMaxWidth()
        )

        HorizontalDivider(
            color = MaterialTheme.colorScheme.outlineVariant
        )

        if (isLoading && notifications.isEmpty()) {
            androidx.compose.material3.CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(32.dp)
            )
        } else if (hasLoadError && notifications.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxWidth().weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.notifications_load_error),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                androidx.compose.material3.TextButton(onClick = onRetry) {
                    Text(text = stringResource(R.string.notifications_retry))
                }
            }
        } else if (notifications.isEmpty()) {

            NotificationsEmptySection(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

        } else {

            val todayNotifications = notifications.filter {
                android.text.format.DateUtils.isToday(it.timestampMillis)
            }
            val previousNotifications = notifications.filterNot {
                android.text.format.DateUtils.isToday(it.timestampMillis)
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {

                item {
                    NotificationSectionTitle(
                        title = stringResource(R.string.hoy)
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
                            title = stringResource(R.string.anteriores)
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
            text = stringResource(R.string.no_tienes_notificaciones),
            modifier = Modifier.padding(top = 16.dp),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = stringResource(R.string.cuando_tengas_nuevas_notificaciones_aparecer_n_aqu),
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
        NotificationsContent(
            notifications = PreviewData.notifications
        )
    }
}
