package com.example.wayspot.ui.screens.notifications

import com.example.wayspot.data.model.Notification

data class NotificationsState(
    val notifications: List<Notification> = emptyList()
)