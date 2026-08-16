package com.example.wayspot.model

data class Places(
    val titulo: String,
    val categoria: String,
    val ubicacion: String,
    val rating: Double = 5.0,
    val imagen: Any? = null,
    val isSaved: Boolean = false
)
