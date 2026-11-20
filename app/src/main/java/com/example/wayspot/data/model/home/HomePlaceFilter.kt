package com.example.wayspot.data.model.home

import com.example.wayspot.data.model.place.PlaceInfo
import java.text.Normalizer
import java.util.Locale

/** Reglas de búsqueda y filtrado sobre lugares reales del catálogo. */
object HomePlaceFilter {

    /** Devuelve únicamente las categorías disponibles, sin duplicados ni valores vacíos. */
    fun categories(places: List<PlaceInfo>): List<String> = places
        .map { it.category.trim() }
        .filter { it.isNotBlank() }
        .distinctBy { normalize(it) }
        .sortedBy { normalize(it) }

    /** La búsqueda por texto y la categoría seleccionada se combinan. */
    fun filter(
        places: List<PlaceInfo>,
        query: String,
        category: String?
    ): List<PlaceInfo> {
        val normalizedQuery = normalize(query)
        val normalizedCategory = category?.let(::normalize)?.takeIf { it.isNotEmpty() }

        return places.filter { place ->
            val matchesCategory = normalizedCategory == null ||
                normalize(place.category) == normalizedCategory
            val matchesQuery = normalizedQuery.isEmpty() ||
                listOf(place.title, place.location, place.category)
                    .any { normalize(it).contains(normalizedQuery) }

            matchesCategory && matchesQuery
        }
    }

    private fun normalize(value: String): String = Normalizer
        .normalize(value.trim().lowercase(Locale.ROOT), Normalizer.Form.NFD)
        .replace(Regex("\\p{Mn}+"), "")
}
