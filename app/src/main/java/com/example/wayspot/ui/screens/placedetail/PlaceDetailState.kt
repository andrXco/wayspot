package com.example.wayspot.ui.screens.placedetail

import com.example.wayspot.data.model.Place

/** Estado del detalle mientras se resuelve el lugar solicitado por la ruta. */
data class PlaceDetailState(
    val place: Place? = null
)
