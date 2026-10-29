package com.example.wayspot.data.model

import com.example.wayspot.data.local.PreviewDataPopular
import org.junit.Assert.assertEquals
import org.junit.Test

class PlaceInfoTest {

    @Test
    fun localPlaceMapsToDetailFallback() {
        val source = PreviewDataPopular.samplePlaces1
        val mapped = source.toPlaceInfo { resourceId ->
            "resource:$resourceId"
        }

        assertEquals(source.id, mapped.id)
        assertEquals("resource:${source.tituloRes}", mapped.title)
        assertEquals("resource:${source.categoriaRes}", mapped.category)
        assertEquals("resource:${source.ubicacionRes}", mapped.location)
        assertEquals(source.rating, mapped.rating, 0.0)
        assertEquals(source.imagen, mapped.imageUrl)
        assertEquals(
            "resource:${source.detail.descriptionRes}",
            mapped.description
        )
    }
}
