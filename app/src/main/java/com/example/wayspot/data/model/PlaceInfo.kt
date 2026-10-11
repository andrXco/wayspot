package com.example.wayspot.data.model

data class PlaceInfo(
    val id: String,
    val title: String,
    val category: String,
    val location: String,
    val rating: Double,
    val imageUrl: String?,
    val description: String,
    val slug: String = id,
    val duration: String? = null,
    val price: String? = null,
    val altitude: String? = null,
    val reviewCount: Int = 0
)

/** Adapta un lugar localizado del catálogo a la representación usada por el detalle remoto. */
internal fun Place.toPlaceInfo(
    resolveString: (Int) -> String
): PlaceInfo = PlaceInfo(
    id = id,
    title = titleText ?: resolveString(tituloRes),
    category = categoryText ?: resolveString(categoriaRes),
    location = locationText ?: resolveString(ubicacionRes),
    rating = rating,
    imageUrl = imagen,
    description = detail.descriptionText ?: resolveString(detail.descriptionRes),
    slug = id,
    duration = detail.durationText,
    price = detail.priceText,
    altitude = detail.altitudeText,
    reviewCount = detail.reviewCount
)
