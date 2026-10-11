package com.example.wayspot.data.model

import androidx.annotation.StringRes
import com.example.wayspot.R

/** Categoría de exploración que relaciona una etiqueta visible con categorías de lugares. */
data class ExploreCategory(
    @param:StringRes val labelRes: Int,
    val placeCategoryResIds: Set<Int>,
    val showsAllPlaces: Boolean = false,
    val placeCategoryNames: Set<String> = emptySet()
)

/** Define las categorías disponibles y el filtrado determinista del catálogo local. */
object ExploreCategoryRules {
    val categories = listOf(
        ExploreCategory(
            labelRes = R.string.category_all,
            placeCategoryResIds = emptySet(),
            showsAllPlaces = true
        ),
        ExploreCategory(
            labelRes = R.string.category_beaches,
            placeCategoryResIds = setOf(R.string.place_category_beach),
            placeCategoryNames = setOf("Playa")
        ),
        ExploreCategory(
            labelRes = R.string.category_mountains,
            placeCategoryResIds = setOf(R.string.place_category_mountain),
            placeCategoryNames = setOf("Montaña")
        ),
        ExploreCategory(
            labelRes = R.string.category_museums,
            placeCategoryResIds = setOf(R.string.place_category_museum),
            placeCategoryNames = setOf("Museo")
        ),
        ExploreCategory(
            labelRes = R.string.category_parks,
            placeCategoryResIds = setOf(
                R.string.place_category_park,
                R.string.place_category_nature
            ),
            placeCategoryNames = setOf("Parque", "Naturaleza")
        ),
        ExploreCategory(
            labelRes = R.string.category_cities,
            placeCategoryResIds = setOf(R.string.place_category_city),
            placeCategoryNames = setOf("Ciudad")
        ),
        ExploreCategory(
            labelRes = R.string.category_art,
            placeCategoryResIds = setOf(R.string.place_category_heritage),
            placeCategoryNames = setOf("Patrimonio")
        )
    )

    val initialCategory = categories.first()

    fun filterPlaces(
        places: List<Place>,
        selectedCategory: ExploreCategory
    ): List<Place> {
        if (selectedCategory.showsAllPlaces) {
            return places
        }

        return places.filter { place ->
            place.categoriaRes in selectedCategory.placeCategoryResIds ||
                place.categoryText in selectedCategory.placeCategoryNames
        }
    }
}
