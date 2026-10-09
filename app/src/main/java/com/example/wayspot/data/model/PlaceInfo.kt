package com.example.wayspot.data.model

data class PlaceInfo(
    val id: String,
    val title: String,
    val category: String,
    val location: String,
    val rating: Double,
    val imageUrl: String?,
    val description: String
)

/** Adapta un lugar localizado del catálogo a la representación usada por el detalle remoto. */
internal fun Place.toPlaceInfo(
    resolveString: (Int) -> String
): PlaceInfo = PlaceInfo(
    id = id,
    title = resolveString(tituloRes),
    category = resolveString(categoriaRes),
    location = resolveString(ubicacionRes),
    rating = rating,
    imageUrl = imagen,
    description = resolveString(detail.descriptionRes)
)
