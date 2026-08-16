package com.example.wayspot.ui.preview

import com.example.wayspot.R
import com.example.wayspot.model.PlaceDetail
import com.example.wayspot.model.PlaceReview
import com.example.wayspot.model.Places

object PreviewDataPopular {

    private val machuPicchuDetail = PlaceDetail(
        durationRes = R.string.place_machu_picchu_duration,
        priceRes = R.string.place_machu_picchu_price,
        altitudeRes = R.string.place_machu_picchu_altitude,
        descriptionRes = R.string.place_machu_picchu_description,
        reviewCount = 1243,
        recentReviews = listOf(
            PlaceReview(
                userNameRes = R.string.user_name_sample,
                dateRes = R.string.review_date_sample,
                rating = 5,
                commentRes = R.string.place_machu_picchu_review
            )
        )
    )

    val samplePlaces1 = Places(
        id = "machu_picchu",
        tituloRes = R.string.place_machu_picchu_title,
        categoriaRes = R.string.place_category_heritage,
        ubicacionRes = R.string.place_machu_picchu_location,
        rating = 4.9,
        imagen = R.drawable.post_card_machu_pichu,
        detail = machuPicchuDetail
    )

    val samplePlaces2 = Places(
        id = "maldives_beach",
        tituloRes = R.string.place_maldives_title,
        categoriaRes = R.string.place_category_beach,
        ubicacionRes = R.string.place_maldives_location,
        rating = 4.8,
        imagen = "https://images.unsplash.com/photo-1514282401047-d79a71a590e8?q=80&w=1000&auto=format&fit=crop",
        detail = PlaceDetail(
            durationRes = R.string.place_maldives_duration,
            priceRes = R.string.place_maldives_price,
            altitudeRes = R.string.place_maldives_altitude,
            descriptionRes = R.string.place_maldives_description,
            reviewCount = 892,
            recentReviews = emptyList()
        )
    )

    val samplePlaces3 = Places(
        id = "louvre",
        tituloRes = R.string.place_louvre_title,
        categoriaRes = R.string.place_category_museum,
        ubicacionRes = R.string.place_louvre_location,
        rating = 4.7,
        imagen = "https://images.unsplash.com/photo-1597923891185-303780189671?q=80&w=1000&auto=format&fit=crop",
        detail = PlaceDetail(
            durationRes = R.string.place_louvre_duration,
            priceRes = R.string.place_louvre_price,
            altitudeRes = R.string.place_louvre_altitude,
            descriptionRes = R.string.place_louvre_description,
            reviewCount = 2134,
            recentReviews = emptyList()
        )
    )

    val samplePlaces4 = Places(
        id = "swiss_alps",
        tituloRes = R.string.place_swiss_alps_title,
        categoriaRes = R.string.place_category_mountain,
        ubicacionRes = R.string.place_swiss_alps_location,
        rating = 4.9,
        imagen = "https://images.unsplash.com/photo-1531310197839-ccf54634509e?q=80&w=1000&auto=format&fit=crop",
        detail = PlaceDetail(
            durationRes = R.string.place_swiss_alps_duration,
            priceRes = R.string.place_swiss_alps_price,
            altitudeRes = R.string.place_swiss_alps_altitude,
            descriptionRes = R.string.place_swiss_alps_description,
            reviewCount = 1678,
            recentReviews = emptyList()
        )
    )

    val samplePlaces5 = Places(
        id = "central_park",
        tituloRes = R.string.place_central_park_title,
        categoriaRes = R.string.place_category_park,
        ubicacionRes = R.string.place_central_park_location,
        rating = 4.6,
        imagen = "https://images.unsplash.com/photo-1526281216101-e55f00f0db7a?q=80&w=1000&auto=format&fit=crop",
        detail = PlaceDetail(
            durationRes = R.string.place_central_park_duration,
            priceRes = R.string.place_central_park_price,
            altitudeRes = R.string.place_central_park_altitude,
            descriptionRes = R.string.place_central_park_description,
            reviewCount = 944,
            recentReviews = emptyList()
        )
    )

    val samplePlaces6 = Places(
        id = "eiffel_tower",
        tituloRes = R.string.place_eiffel_tower_title,
        categoriaRes = R.string.place_category_city,
        ubicacionRes = R.string.place_eiffel_tower_location,
        rating = 4.8,
        imagen = "https://images.unsplash.com/photo-1511739001486-6bfe10ce785f?q=80&w=1000&auto=format&fit=crop",
        detail = PlaceDetail(
            durationRes = R.string.place_eiffel_tower_duration,
            priceRes = R.string.place_eiffel_tower_price,
            altitudeRes = R.string.place_eiffel_tower_altitude,
            descriptionRes = R.string.place_eiffel_tower_description,
            reviewCount = 3256,
            recentReviews = emptyList()
        )
    )

    val listPlaces = listOf(
        samplePlaces1, samplePlaces2, samplePlaces3, 
        samplePlaces4, samplePlaces5, samplePlaces6
    )
}
