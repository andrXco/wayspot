package com.example.wayspot.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.wayspot.ui.screens.splash.SplashScreen
import com.example.wayspot.ui.screens.auth.login.LoginScreen
import com.example.wayspot.ui.screens.auth.signup.SignUpScreen
import com.example.wayspot.data.local.PreviewData
import com.example.wayspot.data.model.SavedPlacesRules
import com.example.wayspot.ui.screens.home.HomeScreen
import androidx.compose.runtime.setValue
import com.example.wayspot.ui.screens.notifications.NotificationsScreen
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.wayspot.data.local.PreviewDataPopular
import com.example.wayspot.ui.screens.placedetail.PlaceDetailScreen
import com.example.wayspot.ui.screens.newreview.NewReviewScreen
import com.example.wayspot.ui.screens.explore.ExploreScreen
import com.example.wayspot.ui.screens.profile.ProfileScreen
import com.example.wayspot.ui.screens.editprofile.EditProfileScreen
import com.example.wayspot.ui.screens.savedplaces.SavedPlacesScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    var userProfile by remember {
        mutableStateOf(PreviewData.userProfile)
    }

    var savedPlaces by remember {
        mutableStateOf(PreviewData.savedPlaces)
    }

    NavHost(
        navController = navController,
        startDestination = "splash",
        modifier = modifier

    ) {

        composable("saved_places") {
            SavedPlacesScreen(
                savedPlaces = savedPlaces,

                onBackClick = {
                    navController.navigate("profile")
                },

                onPlaceClick = { place ->
                    navController.navigate("place_detail/${place.id}")
                },

                onRemoveFromList = { placeId, list ->
                    savedPlaces = SavedPlacesRules.removeFromList(
                        savedPlaces = savedPlaces,
                        placeId = placeId,
                        list = list
                    )
                }
            )
        }

        composable("edit_profile") {
            EditProfileScreen(
                profile = userProfile,

                onBackClick = {
                    navController.navigate("profile")
                },

                onSaveClick = { updatedProfile ->
                    userProfile = updatedProfile
                    navController.navigate("profile")
                },

                onDeleteAccountConfirmed = {
                    userProfile = PreviewData.userProfile
                    savedPlaces = PreviewData.savedPlaces
                    navController.navigate("login")
                }
            )
        }

        composable("profile") {
            ProfileScreen(
                userProfile = userProfile,
                onEditProfileClick = {
                    navController.navigate("edit_profile")
                },
                onSavedPlacesClick = {
                    navController.navigate("saved_places")
                }
            )
        }

        composable("explore") {
            ExploreScreen(
                onPlaceClick = { place ->
                    navController.navigate("place_detail/${place.id}")
                },
                savedPlaces = savedPlaces,
                onSaveClick = { place ->
                    savedPlaces = SavedPlacesRules.toggleSaved(
                        savedPlaces = savedPlaces,
                        place = place
                    )
                }
            )
        }

        composable(
            route = "place_detail/{placeId}",
            arguments = listOf(
                navArgument("placeId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val placeId = backStackEntry.arguments?.getString("placeId")

            val place = PreviewDataPopular.listPlaces.find {
                it.id == placeId
            }

            if (place != null) {
                PlaceDetailScreen(
                    place = place,
                    onBackClick = {
                        navController.navigate("home")
                    },
                    onWriteReviewClick = {
                        navController.navigate("new_review/${place.id}")
                    }
                )
            }
        }

        composable("splash") {
            SplashScreen(
                onLoginClick = {
                    navController.navigate("login")
                },
                onSignUpClick = {
                    navController.navigate("signup")
                }
            )
        }

        composable("login") {
            LoginScreen(
                onLoginClick = {
                    navController.navigate("home")
                },
                onSignUpClick = {
                    navController.navigate("signup")
                }
            )
        }

        composable("notifications") {
            NotificationsScreen(
                onBackClick = {
                    navController.navigate("home")
                }
            )
        }

        composable("home") {
            HomeScreen(
                onNotificationsClick = {
                    navController.navigate("notifications")
                },
                onPlaceClick = { place ->
                    navController.navigate("place_detail/${place.id}")
                }
            )
        }

        composable("signup") {
            SignUpScreen(
                onSignUpClick = {
                    navController.navigate("login")
                },
                onBackToLoginClick = {
                    navController.navigate("login")
                }
            )
        }

        composable(
            route = "new_review/{placeId}",
            arguments = listOf(
                navArgument("placeId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val placeId = backStackEntry.arguments?.getString("placeId")

            val place = PreviewDataPopular.listPlaces.find {
                it.id == placeId
            }

            if (place != null) {
                NewReviewScreen(
                    place = place,
                    onBackClick = {
                        navController.navigate("place_detail/${place.id}")
                    }
                )
            }
        }
    }
}