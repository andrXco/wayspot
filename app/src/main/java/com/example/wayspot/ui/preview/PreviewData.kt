package com.example.wayspot.ui.preview

import com.example.wayspot.R
import com.example.wayspot.model.Post
import com.example.wayspot.model.Review
import com.example.wayspot.model.NotificationInfo


object PreviewData {
    val samplePost1 = Post(
        nombre = "María González",
        usuario = "@maria_g",
        tiempo = "Hace 2 horas",
        categoria = "Patrimonio",
        titulo = "Machu Picchu",
        ubicacion = "Cusco, Perú",
        descripcion = "Una experiencia que te cambia la vida. Ver el amanecer sobre las ruinas con la neblina entre las montañas es algo que no olvidarás jamás. Recomiendo subir temprano.",
        placeId = "machu_picchu",
        imagen = R.drawable.post_card_machu_pichu
    )

    val samplePost2 = Post(
        nombre = "Carlos Ramírez",
        usuario = "@carlos_r",
        tiempo = "Hace 5 horas",
        categoria = "Arte",
        titulo = "Parque Güell",
        ubicacion = "Barcelona, España",
        descripcion = "El diseño de Gaudí es simplemente surrealista. Las vistas de la ciudad desde la terraza son espectaculares. Compra entradas con anticipación.",
        placeId = "parque_guell",
        imagen = "https://images.unsplash.com/photo-1583422422116-392a74afe882?q=80&w=1000&auto=format&fit=crop"
    )

    val listPosts = listOf(samplePost1, samplePost2)

    val sampleReview = Review(
        usuario = "@viajero99",
        comentario = "Increíble lugar, volvería mil veces.",
        rating = 5,
        fecha = "12/08/2026"
    )

    val notifications = listOf(
        NotificationInfo(
            id = 1,
            username = "Laura",
            message = "comenzó a seguirte",
            time = "Hace 5 min"
        ),
        NotificationInfo(
            id = 2,
            username = "Andrés",
            message = "le dio me gusta a tu reseña",
            time = "Hace 20 min"
        ),
        NotificationInfo(
            id = 3,
            username = "Camila",
            message = "comentó tu reseña de Monserrate",
            time = "Hace 1 h"
        ),
        NotificationInfo(
            id = 4,
            username = "Sofía",
            message = "publicó una nueva reseña",
            time = "Hace 3 h"
        ),
        NotificationInfo(
            id = 5,
            username = "Daniel",
            message = "comenzó a seguirte",
            time = "Ayer"
        )
    )

    val reviews = listOf(
        Review(
            usuario = "Valentina García",
            comentario = "Una experiencia increíble. La vista y el recorrido valen completamente la pena.",
            rating = 5,
            fecha = "12 ago 2026"
        ),
        Review(
            usuario = "Valentina García",
            comentario = "Un lugar que definitivamente volvería a visitar.",
            rating = 4,
            fecha = "3 jul 2026"
        )
    )

    val listReviews = listOf(
        Review(
            usuario = "@valentina_viaja",
            comentario = "Una experiencia increíble. La vista y el recorrido valen completamente la pena.",
            rating = 5,
            fecha = "12/08/2026"
        ),
        Review(
            usuario = "@valentina_viaja",
            comentario = "Un lugar que definitivamente volvería a visitar.",
            rating = 4,
            fecha = "03/07/2026"
        ),
        Review(
            usuario = "@valentina_viaja",
            comentario = "Muy bonito y tranquilo, ideal para pasar la tarde.",
            rating = 5,
            fecha = "28/06/2026"
        ),
        Review(
            usuario = "@valentina_viaja",
            comentario = "La experiencia fue buena, aunque había bastante gente.",
            rating = 4,
            fecha = "15/06/2026"
        ),
        Review(
            usuario = "@valentina_viaja",
            comentario = "La vista fue lo mejor de toda la visita.",
            rating = 5,
            fecha = "02/06/2026"
        )
    )
}
