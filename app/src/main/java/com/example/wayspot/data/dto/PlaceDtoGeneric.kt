package com.example.wayspot.data.dto

import com.example.wayspot.data.model.place.Place
import com.example.wayspot.data.model.place.PlaceInfo

abstract class PlaceDtoGeneric {

    abstract fun toPlaceInfo(): PlaceInfo

    abstract fun toPlace(): Place
}
