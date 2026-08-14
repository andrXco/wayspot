package com.example.wayspot.ui.preview

import com.example.wayspot.model.Post
import com.example.wayspot.model.Review

object PreviewData {
    val samplePost1 = Post(
        nombre = "María González",
        usuario = "@maria_g",
        tiempo = "Hace 2 horas",
        categoria = "Patrimonio",
        titulo = "Machu Picchu",
        ubicacion = "Cusco, Perú",
        descripcion = "Una experiencia que te cambia la vida. Ver el amanecer sobre las ruinas con la neblina entre las montañas es algo que no olvidarás jamás. Recomiendo subir temprano."
    )

    val samplePost2 = Post(
        nombre = "Carlos Ramírez",
        usuario = "@carlos_r",
        tiempo = "Hace 5 horas",
        categoria = "Arte",
        titulo = "Parque Güell",
        ubicacion = "Barcelona, España",
        descripcion = "El diseño de Gaudí es simplemente surrealista. Las vistas de la ciudad desde la terraza son espectaculares. Compra entradas con anticipación."
    )

    val listPosts = listOf(samplePost1, samplePost2)

    val sampleReview = Review(
        usuario = "@viajero99",
        comentario = "Increíble lugar, volvería mil veces.",
        rating = 5,
        fecha = "12/08/2026"
    )
}
