package com.example.wayspot.data.model

import androidx.annotation.StringRes
import com.example.wayspot.R

data class ExploreCategory(
    @param:StringRes val labelRes: Int,
    val placeCategoryResIds: Set<Int>,
    val showsAllPlaces: Boolean = false
)

object ExploreCategoryRules {
    val categories = listOf(
        ExploreCategory(
            labelRes = R.string.category_all,
            placeCategoryResIds = emptySet(),
            showsAllPlaces = true
        ),
        ExploreCategory(
            labelRes = R.string.category_beaches,
            placeCategoryResIds = setOf(R.string.place_category_beach)
        ),
        ExploreCategory(
            labelRes = R.string.category_mountains,
            placeCategoryResIds = setOf(R.string.place_category_mountain)
        ),
        ExploreCategory(
            labelRes = R.string.category_museums,
            placeCategoryResIds = setOf(R.string.place_category_museum)
        ),
        ExploreCategory(
            labelRes = R.string.category_parks,
            placeCategoryResIds = setOf(
                R.string.place_category_park,
                R.string.place_category_nature
            )
        ),
        ExploreCategory(
            labelRes = R.string.category_cities,
            placeCategoryResIds = setOf(R.string.place_category_city)
        ),
        ExploreCategory(
            labelRes = R.string.category_art,
            placeCategoryResIds = setOf(R.string.place_category_heritage)
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
            place.categoriaRes in selectedCategory.placeCategoryResIds
        }
    }
}
