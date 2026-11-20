package com.example.wayspot.data.model.explore

import com.example.wayspot.data.model.place.Place
import java.text.Normalizer
import java.util.Locale

/** Búsqueda y categorías derivadas de los lugares que devuelve Firestore. */
object ExplorePlaceFilter {
    fun categories(places: List<Place>): List<String> = places
        .mapNotNull { it.categoryText?.trim() }
        .filter { it.isNotBlank() }
        .distinctBy(::normalize)
        .sortedBy(::normalize)

    fun filter(places: List<Place>, query: String, category: String?): List<Place> {
        val searched = normalize(query)
        val chosenCategory = category?.let(::normalize)?.takeIf { it.isNotEmpty() }

        return places.filter { place ->
            val categoryText = place.categoryText.orEmpty()
            val categoryMatches = chosenCategory == null || normalize(categoryText) == chosenCategory
            val queryMatches = searched.isEmpty() || listOf(
                place.titleText.orEmpty(),
                place.locationText.orEmpty(),
                categoryText
            ).any { normalize(it).contains(searched) }
            categoryMatches && queryMatches
        }
    }

    private fun normalize(value: String): String = Normalizer
        .normalize(value.trim().lowercase(Locale.ROOT), Normalizer.Form.NFD)
        .replace(Regex("\\p{Mn}+"), "")
}
