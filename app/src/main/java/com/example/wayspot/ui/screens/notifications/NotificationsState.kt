package com.example.wayspot.ui.screens.notifications

import com.example.wayspot.data.model.Notification

/** Estado inmutable de las notificaciones disponibles para la pantalla. */
data class NotificationsState(
    val notifications: List<Notification> = emptyList()
)
