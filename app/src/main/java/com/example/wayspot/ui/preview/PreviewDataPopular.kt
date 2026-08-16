package com.example.wayspot.ui.preview

import com.example.wayspot.R
import com.example.wayspot.model.Places

object PreviewDataPopular {

    val samplePlaces1 = Places(
        titulo = "Machu Picchu",
        categoria = "Patrimonio",
        ubicacion = "Cusco, Perú",
        rating = 4.9,
        imagen = R.drawable.post_card_machu_pichu
    )

    val samplePlaces2 = Places(
        titulo = "Playa Maldivas",
        categoria = "Playa",
        ubicacion = "Malé, Maldivas",
        rating = 4.8,
        imagen = "https://images.unsplash.com/photo-1514282401047-d79a71a590e8?q=80&w=1000&auto=format&fit=crop"
    )

    val samplePlaces3 = Places(
        titulo = "Museo del Louvre",
        categoria = "Museo",
        ubicacion = "París, Francia",
        rating = 4.7,
        imagen = "https://images.unsplash.com/photo-1597923891185-303780189671?q=80&w=1000&auto=format&fit=crop"
    )

    val samplePlaces4 = Places(
        titulo = "Alpes Suizos",
        categoria = "Montaña",
        ubicacion = "Interlaken, Suiza",
        rating = 4.9,
        imagen = "https://images.unsplash.com/photo-1531310197839-ccf54634509e?q=80&w=1000&auto=format&fit=crop"
    )

    val samplePlaces5 = Places(
        titulo = "Central Park",
        categoria = "Parque",
        ubicacion = "Nueva York, EE.UU.",
        rating = 4.6,
        imagen = "https://images.unsplash.com/photo-1526281216101-e55f00f0db7a?q=80&w=1000&auto=format&fit=crop"
    )

    val samplePlaces6 = Places(
        titulo = "Torre Eiffel",
        categoria = "Ciudad",
        ubicacion = "París, Francia",
        rating = 4.8,
        imagen = "https://images.unsplash.com/photo-1511739001486-6bfe10ce785f?q=80&w=1000&auto=format&fit=crop"
    )

    val listPlaces = listOf(
        samplePlaces1, samplePlaces2, samplePlaces3, 
        samplePlaces4, samplePlaces5, samplePlaces6
    )
}
