package com.example.wayspot.data.local

import com.example.wayspot.R
import com.example.wayspot.data.model.PlaceDetail
import com.example.wayspot.data.model.PlaceReview
import com.example.wayspot.data.model.Place

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

    val samplePlaces1 = Place(
        id = "machu_picchu",
        tituloRes = R.string.place_machu_picchu_title,
        categoriaRes = R.string.place_category_heritage,
        ubicacionRes = R.string.place_machu_picchu_location,
        rating = 4.9,
        imagen = R.drawable.post_card_machu_pichu,
        detail = machuPicchuDetail
    )

    val samplePlaces2 = Place(
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

    val samplePlaces3 = Place(
        id = "louvre",
        tituloRes = R.string.place_louvre_title,
        categoriaRes = R.string.place_category_museum,
        ubicacionRes = R.string.place_louvre_location,
        rating = 4.7,
        imagen = "https://4.bp.blogspot.com/-WRjy3L5mWWw/VlWIxn8KkVI/AAAAAAAAACo/g50ctsdSRC8/s1600/museo-del-louvre_5801441.jpg",
        detail = PlaceDetail(
            durationRes = R.string.place_louvre_duration,
            priceRes = R.string.place_louvre_price,
            altitudeRes = R.string.place_louvre_altitude,
            descriptionRes = R.string.place_louvre_description,
            reviewCount = 2134,
            recentReviews = emptyList()
        )
    )

    val samplePlaces4 = Place(
        id = "swiss_alps",
        tituloRes = R.string.place_swiss_alps_title,
        categoriaRes = R.string.place_category_mountain,
        ubicacionRes = R.string.place_swiss_alps_location,
        rating = 4.9,
        imagen = "https://www.postposmo.com/wp-content/uploads/2020/10/alpes-suizos-18.jpg",
        detail = PlaceDetail(
            durationRes = R.string.place_swiss_alps_duration,
            priceRes = R.string.place_swiss_alps_price,
            altitudeRes = R.string.place_swiss_alps_altitude,
            descriptionRes = R.string.place_swiss_alps_description,
            reviewCount = 1678,
            recentReviews = emptyList()
        )
    )

    val samplePlaces5 = Place(
        id = "central_park",
        tituloRes = R.string.place_central_park_title,
        categoriaRes = R.string.place_category_park,
        ubicacionRes = R.string.place_central_park_location,
        rating = 4.6,
        imagen = "https://park.marmaranyc.com/hs-fs/hubfs/Explore%20Widget/Central%20Park.jpg?width=2000&height=1333&name=Central%20Park.jpg",
        detail = PlaceDetail(
            durationRes = R.string.place_central_park_duration,
            priceRes = R.string.place_central_park_price,
            altitudeRes = R.string.place_central_park_altitude,
            descriptionRes = R.string.place_central_park_description,
            reviewCount = 944,
            recentReviews = emptyList()
        )
    )

    val samplePlaces6 = Place(
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

    val playaBlanca = Place(
        id = "playa_blanca",
        tituloRes = R.string.place_playa_blanca_title,
        categoriaRes = R.string.place_category_beach,
        ubicacionRes = R.string.place_playa_blanca_location,
        rating = 4.7,
        imagen = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?q=80&w=1000&auto=format&fit=crop",
        detail = PlaceDetail(
            durationRes = R.string.place_playa_blanca_duration,
            priceRes = R.string.place_playa_blanca_price,
            altitudeRes = R.string.place_playa_blanca_altitude,
            descriptionRes = R.string.place_playa_blanca_description,
            reviewCount = 738,
            recentReviews = emptyList()
        )
    )

    val parqueTayrona = Place(
        id = "parque_tayrona",
        tituloRes = R.string.place_parque_tayrona_title,
        categoriaRes = R.string.place_category_nature,
        ubicacionRes = R.string.place_parque_tayrona_location,
        rating = 4.8,
        imagen = "https://images.unsplash.com/photo-1518509562904-e7ef99cdcc86?q=80&w=1000&auto=format&fit=crop",
        detail = PlaceDetail(
            durationRes = R.string.place_parque_tayrona_duration,
            priceRes = R.string.place_parque_tayrona_price,
            altitudeRes = R.string.place_parque_tayrona_altitude,
            descriptionRes = R.string.place_parque_tayrona_description,
            reviewCount = 1164,
            recentReviews = emptyList()
        )
    )

    val piedraDelPenol = Place(
        id = "piedra_del_penol",
        tituloRes = R.string.place_piedra_del_penol_title,
        categoriaRes = R.string.place_category_mountain,
        ubicacionRes = R.string.place_piedra_del_penol_location,
        rating = 4.8,
        imagen = "https://i.redd.it/y10vhdjst9901.jpg",
        detail = PlaceDetail(
            durationRes = R.string.place_piedra_del_penol_duration,
            priceRes = R.string.place_piedra_del_penol_price,
            altitudeRes = R.string.place_piedra_del_penol_altitude,
            descriptionRes = R.string.place_piedra_del_penol_description,
            reviewCount = 965,
            recentReviews = emptyList()
        )
    )

    val canoCristales = Place(
        id = "cano_cristales",
        tituloRes = R.string.place_cano_cristales_title,
        categoriaRes = R.string.place_category_nature,
        ubicacionRes = R.string.place_cano_cristales_location,
        rating = 4.9,
        imagen = "https://i.pinimg.com/originals/8f/be/f6/8fbef6eca139d3513f7381c9416fea3e.jpg",
        detail = PlaceDetail(
            durationRes = R.string.place_cano_cristales_duration,
            priceRes = R.string.place_cano_cristales_price,
            altitudeRes = R.string.place_cano_cristales_altitude,
            descriptionRes = R.string.place_cano_cristales_description,
            reviewCount = 624,
            recentReviews = emptyList()
        )
    )

    val parqueGuell = Place(
        id = "parque_guell",
        tituloRes = R.string.place_parque_guell_title,
        categoriaRes = R.string.place_category_park,
        ubicacionRes = R.string.place_parque_guell_location,
        rating = 4.8,
        imagen = "https://cdn-imgix.headout.com/microbrands-content-image/image/e8be31fae465f686757c60e3ebc8d62f-barcelona%20park%20g%C3%BCell.jpg",
        detail = PlaceDetail(
            durationRes = R.string.place_parque_guell_duration,
            priceRes = R.string.place_parque_guell_price,
            altitudeRes = R.string.place_parque_guell_altitude,
            descriptionRes = R.string.place_parque_guell_description,
            reviewCount = 1521,
            recentReviews = emptyList()
        )
    )

    val listPlaces = listOf(
        samplePlaces1,
        samplePlaces2,
        samplePlaces3,
        samplePlaces4,
        samplePlaces5,
        samplePlaces6,
        playaBlanca,
        parqueTayrona,
        piedraDelPenol,
        canoCristales,
        parqueGuell
    )
}
