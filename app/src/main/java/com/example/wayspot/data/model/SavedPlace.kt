package com.example.wayspot.data.model

enum class SavedPlaceList {
    WANT_TO_VISIT,
    FAVORITES,
    VISITED
}

data class SavedPlace(
    val place: Place,
    val lists: Set<SavedPlaceList>
)

object SavedPlacesRules {
    val defaultList = SavedPlaceList.WANT_TO_VISIT

    fun savedPlaceIds(savedPlaces: List<SavedPlace>): Set<String> =
        savedPlaces.mapTo(mutableSetOf()) { savedPlace ->
            savedPlace.place.id
        }

    fun countByList(savedPlaces: List<SavedPlace>): Map<SavedPlaceList, Int> =
        SavedPlaceList.entries.associateWith { list ->
            savedPlaces.count { savedPlace ->
                list in savedPlace.lists
            }
        }

    fun filterByList(
        savedPlaces: List<SavedPlace>,
        list: SavedPlaceList
    ): List<SavedPlace> = savedPlaces.filter { savedPlace ->
        list in savedPlace.lists
    }

    fun removeFromList(
        savedPlaces: List<SavedPlace>,
        placeId: String,
        list: SavedPlaceList
    ): List<SavedPlace> = savedPlaces.mapNotNull { entry ->
        if (entry.place.id != placeId) {
            entry
        } else {
            entry.copy(lists = entry.lists - list)
                .takeIf { updatedEntry -> updatedEntry.lists.isNotEmpty() }
        }
    }

    fun toggleSaved(
        savedPlaces: List<SavedPlace>,
        place: Place
    ): List<SavedPlace> = if (savedPlaces.any { entry -> entry.place.id == place.id }) {
        savedPlaces.filterNot { entry -> entry.place.id == place.id }
    } else {
        savedPlaces + SavedPlace(
            place = place,
            lists = setOf(defaultList)
        )
    }
}
