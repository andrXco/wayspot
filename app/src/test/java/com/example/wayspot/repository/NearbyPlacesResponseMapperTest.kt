package com.example.wayspot.repository

import com.example.wayspot.data.mapper.decodeNearbyPlaces
import com.example.wayspot.data.model.place.NearbyPlacesResponseException
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class NearbyPlacesResponseMapperTest {
    // Lee los campos obligatorios de la callable y conserva la distancia al convertir al DTO.
    @Test
    fun decodeNearbyPlaces_mapsNumericCoordinatesAndDistance() {
        val result = decodeNearbyPlaces(
            mapOf(
                "places" to listOf(
                    mapOf(
                        "id" to "lugar-1",
                        "coordinates" to mapOf("latitude" to 4.71, "longitude" to -74),
                        "distanceMeters" to 840
                    )
                )
            )
        ).single()

        assertThat(result.place.id).isEqualTo("lugar-1")
        assertThat(result.place.coordinates?.latitude).isEqualTo(4.71)
        assertThat(result.place.coordinates?.longitude).isEqualTo(-74.0)
        assertThat(result.distanceMeters).isEqualTo(840.0)
    }

    // Una fila incompleta no se interpreta como una búsqueda que encontró cero lugares.
    @Test(expected = NearbyPlacesResponseException::class)
    fun decodeNearbyPlaces_whenRequiredFieldsAreMissing_fails() {
        decodeNearbyPlaces(mapOf("places" to listOf(mapOf("id" to "lugar-1"))))
    }

    // La lista vacía cumple el contrato y representa una búsqueda sin coincidencias.
    @Test
    fun decodeNearbyPlaces_acceptsAnEmptyList() {
        assertThat(decodeNearbyPlaces(mapOf("places" to emptyList<Any>()))).isEmpty()
    }
}
