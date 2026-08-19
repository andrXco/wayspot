package com.example.wayspot.ui.preview

import com.example.wayspot.R
import com.example.wayspot.model.UserProfile
import com.example.wayspot.model.UserReview

object ProfileData {

    val userProfile = UserProfile(
        name = "Valentina García",
        username = "@valentina_viaja",
        bio = "🚀 Viajera apasionada · 23 países visitados · Compartiendo el mundo un lugar a la vez 🌍",
        placesCount = 47,
        reviewsCount = 38,
        followersCount = "1.2k",
        followingCount = 82,
        profileImage = null // We can use initials if null
    )

    val userReviews = listOf(
        UserReview(
            title = "Cerro Monserrate",
            location = "Bogotá, Colombia",
            date = "12 ago 2026",
            rating = 5,
            comment = "Una experiencia espiritual única. La vista desde la cima al atardecer es simplemente...",
            image = R.drawable.post_card_machu_pichu // Temporary image
        ),
        UserReview(
            title = "Machu Picchu",
            location = "Cusco, Perú",
            date = "3 jul 2026",
            rating = 5,
            comment = "Una maravilla del mundo que supera todas las expectativas. El Camino Inca...",
            image = R.drawable.post_card_machu_pichu
        )
    )
}
