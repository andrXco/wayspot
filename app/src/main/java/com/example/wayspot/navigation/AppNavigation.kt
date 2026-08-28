package com.example.wayspot.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.wayspot.data.local.PreviewData
import com.example.wayspot.data.local.PreviewDataPopular
import com.example.wayspot.data.model.SavedPlacesRules
import com.example.wayspot.ui.screens.auth.login.LoginScreen
import com.example.wayspot.ui.screens.auth.signup.SignUpScreen
import com.example.wayspot.ui.screens.editprofile.EditProfileScreen
import com.example.wayspot.ui.screens.explore.ExploreScreen
import com.example.wayspot.ui.screens.home.HomeScreen
import com.example.wayspot.ui.screens.newreview.NewReviewScreen
import com.example.wayspot.ui.screens.notifications.NotificationsScreen
import com.example.wayspot.ui.screens.placedetail.PlaceDetailScreen
import com.example.wayspot.ui.screens.profile.ProfileScreen
import com.example.wayspot.ui.screens.savedplaces.SavedPlacesScreen
import com.example.wayspot.ui.screens.splash.SplashScreen
import com.example.wayspot.ui.screens.auth.forgotpassword.ForgotPasswordScreen

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
        startDestination = Screen.Splash.route,
        modifier = modifier
    ) {

        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                onSendClick = {
                },
                onBackToLoginClick = {
                    navController.navigate(Screen.Login.route)
                }
            )
        }

        composable(Screen.SavedPlaces.route) {
            SavedPlacesScreen(
                savedPlaces = savedPlaces,

                onBackClick = {
                    navController.navigate(Screen.Profile.route)
                },

                onPlaceClick = { place ->
                    navController.navigate(
                        Screen.PlaceDetail.createRoute(place.id)
                    )
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

        composable(Screen.EditProfile.route) {
            EditProfileScreen(
                profile = userProfile,

                onBackClick = {
                    navController.navigate(Screen.Profile.route)
                },

                onSaveClick = { updatedProfile ->
                    userProfile = updatedProfile
                    navController.navigate(Screen.Profile.route)
                },

                onDeleteAccountConfirmed = {
                    userProfile = PreviewData.userProfile
                    savedPlaces = PreviewData.savedPlaces
                    navController.navigate(Screen.Login.route)
                }
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                userProfile = userProfile,

                onEditProfileClick = {
                    navController.navigate(Screen.EditProfile.route)
                },

                onSavedPlacesClick = {
                    navController.navigate(Screen.SavedPlaces.route)
                }
            )
        }

        composable(Screen.Explore.route) {
            ExploreScreen(
                onPlaceClick = { place ->
                    navController.navigate(
                        Screen.PlaceDetail.createRoute(place.id)
                    )
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
            route = Screen.PlaceDetail.route,
            arguments = listOf(
                navArgument("placeId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val placeId = backStackEntry.arguments?.getString("placeId")

            if (placeId != null) {
                PlaceDetailScreen(
                    placeId = placeId,
                    onBackClick = {
                        navController.navigate(Screen.Home.route)
                    },
                    onWriteReviewClick = {
                        navController.navigate(
                            Screen.NewReview.createRoute(placeId)
                        )
                    }
                )
            }
        }

        composable(Screen.Splash.route) {
            SplashScreen(
                onLoginClick = {
                    navController.navigate(Screen.Login.route)
                },

                onSignUpClick = {
                    navController.navigate(Screen.SignUp.route)
                }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onLoginClick = {
                    navController.navigate(Screen.Home.route)
                },
                onSignUpClick = {
                    navController.navigate(Screen.SignUp.route)
                },
                onForgotPasswordClick = {
                    navController.navigate(Screen.ForgotPassword.route)
                }
            )
        }

        composable(Screen.Notifications.route) {
            NotificationsScreen(
                onBackClick = {
                    navController.navigate(Screen.Home.route)
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onNotificationsClick = {
                    navController.navigate(Screen.Notifications.route)
                },

                onPlaceClick = { place ->
                    navController.navigate(
                        Screen.PlaceDetail.createRoute(place.id)
                    )
                }
            )
        }

        composable(Screen.SignUp.route) {
            SignUpScreen(
                onSignUpClick = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                },

                onBackToLoginClick = {
                    navController.navigate(Screen.Login.route)
                }
            )
        }

        composable(
            route = Screen.NewReview.route,
            arguments = listOf(
                navArgument("placeId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val placeId = backStackEntry.arguments?.getString("placeId")

            if (placeId != null) {
                NewReviewScreen(
                    placeId = placeId,
                    onBackClick = {
                        navController.navigate(
                            Screen.PlaceDetail.createRoute(placeId)
                        )
                    }
                )
            }
        }
    }
}