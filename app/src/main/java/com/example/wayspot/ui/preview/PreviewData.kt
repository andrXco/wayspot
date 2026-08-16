package com.example.wayspot.ui.preview

import com.example.wayspot.R
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
        descripcion = "Una experiencia que te cambia la vida. Ver el amanecer sobre las ruinas con la neblina entre las montañas es algo que no olvidarás jamás. Recomiendo subir temprano.",
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
        imagen = "https://images.unsplash.com/photo-1583422422116-392a74afe882?q=80&w=1000&auto=format&fit=crop"
    )

    val samplePost3 = Post(
        nombre = "Elena Torres",
        usuario = "@elena_t",
        tiempo = "Hace 1 día",
        categoria = "Moderna",
        titulo = "Skyline Shinjuku",
        ubicacion = "Tokio, Japón",
        descripcion = "Las luces de neón y la energía de esta ciudad son incomparables. El mirador del Gobierno Metropolitano ofrece una vista gratuita increíble.",
        imagen = "https://images.unsplash.com/photo-1540959733332-eab4deabeeaf?q=80&w=1000&auto=format&fit=crop"
    )

    val samplePost4 = Post(
        nombre = "Juan Pérez",
        usuario = "@juan_p",
        tiempo = "Hace 3 días",
        categoria = "Historia",
        titulo = "Coliseo Romano",
        ubicacion = "Roma, Italia",
        descripcion = "Sentir la historia bajo tus pies es algo indescriptible. Caminar por los alrededores al atardecer es mágico.",
        imagen = "https://images.unsplash.com/photo-1552832230-c0197dd311b5?q=80&w=1000&auto=format&fit=crop"
    )

    val listPosts = listOf(samplePost1, samplePost2)

    val sampleReview = Review(
        usuario = "@viajero99",
        comentario = "Increíble lugar, volvería mil veces.",
        rating = 5,
        fecha = "12/08/2026"
    )
}
