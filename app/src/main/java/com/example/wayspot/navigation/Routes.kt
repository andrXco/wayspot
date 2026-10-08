package com.example.wayspot.navigation

/** Fuente única de las rutas del `NavHost` y de la construcción de destinos con argumentos. */
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

    object ForgotPassword : Screen("forgot_password")

    object PlaceDetail : Screen("place_detail/{placeId}") {
        fun createRoute(placeId: String): String {
            return "place_detail/$placeId"
        }
    }

    object ReviewDetail : Screen("review_detail/{reviewId}") {
        fun createRoute(reviewId: String) = "review_detail/$reviewId"
    }

    object ReviewComment : Screen("review_comment/{reviewId}") {
        fun createRoute(reviewId: String) = "review_comment/$reviewId"
    }

    object PublicProfile : Screen("public_profile/{userId}") {
        fun createRoute(userId: String) = "public_profile/$userId"
    }

    object NewReview : Screen("new_review?placeId={placeId}&reviewId={reviewId}") {
        fun createRoute(placeId: String? = null): String {
            return placeId?.let { "new_review?placeId=$it" } ?: "new_review"
        }

        fun createEditRoute(reviewId: String) = "new_review?reviewId=$reviewId"
    }
}
