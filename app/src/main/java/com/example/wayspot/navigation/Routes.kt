package com.example.wayspot.navigation

sealed class Screen(val route: String) {

    object Splash : Screen("splash")

    object Login : Screen("login")

    object SignUp : Screen("signup")

    object Home : Screen("home")

    object Explore : Screen("explore")

    object Profile : Screen("profile")

    object SavedPlaces : Screen("saved_places")

    object EditProfile : Screen("edit_profile")

    object Notifications : Screen("notifications")

    object PlaceDetail : Screen("place_detail/{placeId}") {
        fun createRoute(placeId: String): String {
            return "place_detail/$placeId"
        }
    }

    object NewReview : Screen("new_review/{placeId}") {
        fun createRoute(placeId: String): String {
            return "new_review/$placeId"
        }
    }
}
