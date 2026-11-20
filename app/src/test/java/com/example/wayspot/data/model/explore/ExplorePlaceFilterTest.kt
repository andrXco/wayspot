package com.example.wayspot.data.model.explore

import com.example.wayspot.data.model.place.Place
import com.example.wayspot.data.model.place.PlaceDetail
import org.junit.Assert.assertEquals
import org.junit.Test

class ExplorePlaceFilterTest {
    private val places = listOf(
        place("one", "Monserrate", "Montaña", "Bogotá, Colombia"),
        place("two", "Central Park", "Parque", "New York, USA"),
        place("three", "Parque Simón Bolívar", "Parque", "Bogotá, Colombia"),
        place("four", "Museo del Oro", "Museo", "Bogotá, Colombia")
    )

    @Test fun categoriesFromFirestoreAreUniqueAndSorted() {
        val repeated = places + place("five", "Otro parque", "parque", "Cali")
        assertEquals(listOf("Montaña", "Museo", "Parque"), ExplorePlaceFilter.categories(repeated))
    }

    @Test fun titleSearchIgnoresCase() {
        assertEquals(listOf("one"), ExplorePlaceFilter.filter(places, "MONSERRATE", null).map { it.id })
    }

    @Test fun citySearchIgnoresAccents() {
        assertEquals(listOf("one", "three", "four"), ExplorePlaceFilter.filter(places, "bogota", null).map { it.id })
    }

    @Test fun categorySearchWorksAsText() {
        assertEquals(listOf("one"), ExplorePlaceFilter.filter(places, "montana", null).map { it.id })
    }

    @Test fun categoryFilterOnly() {
        assertEquals(listOf("two", "three"), ExplorePlaceFilter.filter(places, "", "parque").map { it.id })
    }

    @Test fun categoryAndQueryCombine() {
        assertEquals(listOf("two"), ExplorePlaceFilter.filter(places, "new york", "Parque").map { it.id })
    }

    @Test fun emptyFiltersReturnAll() {
        assertEquals(places, ExplorePlaceFilter.filter(places, "  ", null))
    }

    @Test fun noMatchesReturnEmpty() {
        assertEquals(emptyList<Place>(), ExplorePlaceFilter.filter(places, "saturno", "Museo"))
    }

    @Test fun blankCategoriesAreNotChips() {
        val additional = place("five", "Sin categoria", "   ", "Cali")
        assertEquals(listOf("Montaña", "Museo", "Parque"), ExplorePlaceFilter.categories(places + additional))
    }

    private fun place(id: String, title: String, category: String, location: String) = Place(
        id = id,
        tituloRes = 0,
        categoriaRes = 0,
        ubicacionRes = 0,
        titleText = title,
        categoryText = category,
        locationText = location,
        detail = PlaceDetail(
            durationRes = 0, priceRes = 0, altitudeRes = 0, descriptionRes = 0,
            reviewCount = 0, recentReviews = emptyList()
        )
    )
}
