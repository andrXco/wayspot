package com.example.wayspot.data

data class Post(
    val nombre: String,
    val usuario: String,
    val tiempo: String,
    val categoria: String,
    val titulo: String,
    val ubicacion: String,
    val descripcion: String,
    val placeId: String? = null,
    val rating: Int = 5,
    val imagen: Any? = null
)