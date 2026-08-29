package com.example.wayspot.data.model

data class Review(
    val usuario: String,
    val placeTitle: String = "",
    val location: String = "",
    val imageRes: Int? = null,
    val comentario: String,
    val rating: Int,
    val fecha: String
)
