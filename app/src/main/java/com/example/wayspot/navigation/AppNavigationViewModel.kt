package com.example.wayspot.navigation

import androidx.lifecycle.ViewModel
import com.example.wayspot.data.local.PreviewData
import com.example.wayspot.data.model.Place
import com.example.wayspot.data.model.SavedPlace
import com.example.wayspot.data.model.SavedPlaceList
import com.example.wayspot.data.model.SavedPlacesRules
import com.example.wayspot.data.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AppNavigationViewModel : ViewModel() {

    private val _userProfile = MutableStateFlow(PreviewData.userProfile)
    val userProfile: StateFlow<UserProfile> = _userProfile

    private val _savedPlaces = MutableStateFlow(PreviewData.savedPlaces)
    val savedPlaces: StateFlow<List<SavedPlace>> = _savedPlaces

    fun updateUserProfile(updatedProfile: UserProfile) {
        _userProfile.value = updatedProfile
    }

    fun resetUserProfile() {
        _userProfile.value = PreviewData.userProfile
    }

    fun resetSavedPlaces() {
        _savedPlaces.value = PreviewData.savedPlaces
    }

    fun removeSavedPlace(
        placeId: String,
        list: SavedPlaceList
    ) {
        _savedPlaces.value = SavedPlacesRules.removeFromList(
            savedPlaces = _savedPlaces.value,
            placeId = placeId,
            list = list
        )
    }

    fun toggleSavedPlace(place: Place) {
        _savedPlaces.value = SavedPlacesRules.toggleSaved(
            savedPlaces = _savedPlaces.value,
            place = place
        )
    }
}