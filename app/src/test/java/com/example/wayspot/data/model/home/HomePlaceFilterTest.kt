package com.example.wayspot.data.model.home

import com.example.wayspot.data.model.place.PlaceInfo
import org.junit.Assert.assertEquals
import org.junit.Test

class HomePlaceFilterTest {
    private val places = listOf(
        place("park", "Parque Simón Bolívar", "Parque", "Bogotá, Colombia"),
        place("museum", "Museo del Oro", "Museo", "Bogotá, Colombia"),
        place("central", "Central Park", "parque", "New York, USA"),
        place("restaurant", "La Casona", "Restaurante", "Medellín, Colombia")
    )

    @Test
    fun categoriesAreTakenFromRealPlacesAndDeduplicated() {
        assertEquals(listOf("Museo", "Parque", "Restaurante"), HomePlaceFilter.categories(places))
    }

    @Test
    fun searchFindsPlaceByTitleIgnoringCaseAndAccents() {
        assertEquals(
            listOf("park"),
            HomePlaceFilter.filter(places, "PARQUE SIMON", null).map { it.id }
        )
    }

    @Test
    fun searchFindsPlaceByCity() {
        assertEquals(
            listOf("park", "museum"),
            HomePlaceFilter.filter(places, "bogota", null).map { it.id }
        )
    }

    @Test
    fun searchFindsPlaceByCategory() {
        assertEquals(
            listOf("park", "central"),
            HomePlaceFilter.filter(places, "parque", null).map { it.id }
        )
    }

    @Test
    fun categoryAndQueryAreAppliedTogether() {
        assertEquals(
            listOf("central"),
            HomePlaceFilter.filter(places, "NEW YORK", "Parque").map { it.id }
        )
    }

    @Test
    fun allCategoriesShowsEntireCatalogWhenSearchIsBlank() {
        assertEquals(places, HomePlaceFilter.filter(places, "   ", null))
    }

    @Test
    fun nonMatchingSearchReturnsEmptyList() {
        assertEquals(emptyList<PlaceInfo>(), HomePlaceFilter.filter(places, "Saturno", null))
    }

    @Test
    fun emptyCategoryFieldsAreExcludedFromChips() {
        assertEquals(
            listOf("Museo", "Parque", "Restaurante"),
            HomePlaceFilter.categories(places + place("empty", "Sin categoría", "  ", "Cali"))
        )
    }

    private fun place(id: String, title: String, category: String, location: String) = PlaceInfo(
        id = id,
        title = title,
        category = category,
        location = location,
        rating = 4.0,
        imageUrl = null,
        description = ""
    )
}
