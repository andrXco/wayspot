package com.example.wayspot.ui.preview

import com.example.wayspot.R
import com.example.wayspot.model.Notification

object NotificationsData {

    val notifications = listOf(
        Notification(
            id = 1,
            username = "Laura",
            message = "comenzó a seguirte",
            time = "Hace 5 min",
            image = R.drawable.branding_logo_claro_wayspot,
        ),
        Notification(
            id = 2,
            username = "Andrés",
            message = "le dio me gusta a tu reseña",
            time = "Hace 20 min",
            image = R.drawable.branding_logo_claro_wayspot,
        ),
        Notification(
            id = 3,
            username = "Camila",
            message = "comentó tu reseña de Monserrate",
            time = "Hace 1 h",
            image = R.drawable.branding_logo_claro_wayspot,
        ),
        Notification(
            id = 4,
            username = "Sofía",
            message = "publicó una nueva reseña",
            time = "Hace 3 h",
            image = R.drawable.branding_logo_claro_wayspot,
        ),
        Notification(
            id = 5,
            username = "Daniel",
            message = "comenzó a seguirte",
            time = "Ayer",
            image = R.drawable.branding_logo_claro_wayspot,
        )
    )
}
