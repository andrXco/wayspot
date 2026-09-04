package com.example.wayspot.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.wayspot.ui.screens.auth.forgotpassword.ForgotPasswordScreen
import com.example.wayspot.ui.screens.auth.login.LoginScreen
import com.example.wayspot.ui.screens.auth.login.LoginViewModel
import com.example.wayspot.ui.screens.auth.signup.SignUpScreen
import com.example.wayspot.ui.screens.auth.signup.SignUpViewModel
import com.example.wayspot.ui.screens.editprofile.EditProfileScreen
import com.example.wayspot.ui.screens.explore.ExploreScreen
import com.example.wayspot.ui.screens.home.HomeScreen
import com.example.wayspot.ui.screens.newreview.NewReviewScreen
import com.example.wayspot.ui.screens.notifications.NotificationsScreen
import com.example.wayspot.ui.screens.placedetail.PlaceDetailScreen
import com.example.wayspot.ui.screens.profile.ProfileScreen
import com.example.wayspot.ui.screens.savedplaces.SavedPlacesScreen
import com.example.wayspot.ui.screens.splash.SplashScreen
import com.example.wayspot.ui.screens.auth.forgotpassword.ForgotPasswordViewModel
import com.example.wayspot.ui.screens.editprofile.EditProfileViewModel
import com.example.wayspot.ui.screens.home.HomeViewModel
import com.example.wayspot.ui.screens.explore.ExploreViewModel
import com.example.wayspot.ui.screens.savedplaces.SavedPlacesViewModel
import com.example.wayspot.ui.screens.newreview.NewReviewViewModel
import com.example.wayspot.ui.screens.profile.ProfileViewModel
import com.example.wayspot.ui.screens.notifications.NotificationsViewModel
import com.example.wayspot.ui.screens.placedetail.PlaceDetailViewModel
import com.example.wayspot.ui.screens.splash.SplashViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    val appNavigationViewModel: AppNavigationViewModel = viewModel()

    val appNavigationState by appNavigationViewModel.uiState.collectAsState()
    val userProfile = appNavigationState.userProfile ?: return
    val savedPlaces = appNavigationState.savedPlaces

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier
    ) {

        composable(Screen.ForgotPassword.route) {

            val forgotPasswordViewModel: ForgotPasswordViewModel = viewModel()

            ForgotPasswordScreen(
                forgotPasswordViewModel = forgotPasswordViewModel,

                onSendClick = {
                },

                onBackToLoginClick = {
                    navController.navigate(
                        Screen.Login.route
                    )
                }
            )
        }

        composable(Screen.SavedPlaces.route) {

            val savedPlacesViewModel: SavedPlacesViewModel = viewModel()

            SavedPlacesScreen(
                savedPlacesViewModel = savedPlacesViewModel,
                savedPlaces = savedPlaces,

                onBackClick = {
                    navController.navigate(
                        Screen.Profile.route
                    )
                },

                onPlaceClick = { placeId ->
                    navController.navigate(
                        Screen.PlaceDetail.createRoute(placeId)
                    )
                },

                onRemoveFromList = { placeId, list ->
                    appNavigationViewModel.removeSavedPlace(
                        placeId = placeId,
                        list = list
                    )
                }
            )
        }

        composable(Screen.EditProfile.route) {

            val editProfileViewModel: EditProfileViewModel = viewModel()

            EditProfileScreen(
                editProfileViewModel = editProfileViewModel,
                profile = userProfile,

                onBackClick = {
                    navController.navigate(
                        Screen.Profile.route
                    )
                },

                onSaveClick = { updatedProfile ->
                    appNavigationViewModel.updateUserProfile(
                        updatedProfile
                    )

                    navController.navigate(
                        Screen.Profile.route
                    )
                },

                onDeleteAccountConfirmed = {
                    appNavigationViewModel.resetUserProfile()
                    appNavigationViewModel.resetSavedPlaces()

                    navController.navigate(
                        Screen.Login.route
                    )
                }
            )
        }

        composable(Screen.Profile.route) {

            val profileViewModel: ProfileViewModel = viewModel()

            ProfileScreen(
                profileViewModel = profileViewModel,
                userProfile = userProfile,

                onEditProfileClick = {
                    navController.navigate(
                        Screen.EditProfile.route
                    )
                },

                onSavedPlacesClick = {
                    navController.navigate(
                        Screen.SavedPlaces.route
                    )
                }
            )
        }

        composable(Screen.Explore.route) {

            val exploreViewModel: ExploreViewModel = viewModel()

            ExploreScreen(
                exploreViewModel = exploreViewModel,

                onPlaceClick = { placeId ->
                    navController.navigate(
                        Screen.PlaceDetail.createRoute(placeId)
                    )
                },

                savedPlaces = savedPlaces,

                onSaveClick = { place ->
                    appNavigationViewModel.toggleSavedPlace(
                        place
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

            val placeId =
                backStackEntry.arguments?.getString("placeId")

            if (placeId != null) {

                val placeDetailViewModel: PlaceDetailViewModel = viewModel()

                PlaceDetailScreen(
                    placeDetailViewModel = placeDetailViewModel,
                    placeId = placeId,

                    onBackClick = {
                        navController.navigate(
                            Screen.Home.route
                        )
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

            val splashViewModel: SplashViewModel = viewModel()

            SplashScreen(
                splashViewModel = splashViewModel,

                onLoginClick = {
                    navController.navigate(Screen.Login.route)
                },

                onSignUpClick = {
                    navController.navigate(Screen.SignUp.route)
                }
            )
        }

        composable(Screen.Login.route) {

            val loginViewModel: LoginViewModel = viewModel()

            LoginScreen(
                loginViewModel = loginViewModel,

                onLoginClick = {
                    navController.navigate(
                        Screen.Home.route
                    )
                },

                onSignUpClick = {
                    navController.navigate(
                        Screen.SignUp.route
                    )
                },

                onForgotPasswordClick = {
                    navController.navigate(
                        Screen.ForgotPassword.route
                    )
                }
            )
        }

        composable(Screen.Notifications.route) {

            val notificationsViewModel: NotificationsViewModel = viewModel()

            NotificationsScreen(
                notificationsViewModel = notificationsViewModel,

                onBackClick = {
                    navController.navigate(
                        Screen.Home.route
                    )
                }
            )
        }

        composable(Screen.Home.route) {

            val homeViewModel: HomeViewModel = viewModel()

            HomeScreen(
                homeViewModel = homeViewModel,

                onNotificationsClick = {
                    navController.navigate(
                        Screen.Notifications.route
                    )
                },

                onPlaceClick = { placeId ->
                    navController.navigate(
                        Screen.PlaceDetail.createRoute(placeId)
                    )
                }
            )
        }

        composable(Screen.SignUp.route) {

            val signUpViewModel: SignUpViewModel = viewModel()

            SignUpScreen(
                signUpViewModel = signUpViewModel,

                onSignUpClick = {
                    navController.navigate(
                        Screen.Home.route
                    ) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                },

                onBackToLoginClick = {
                    navController.navigate(
                        Screen.Login.route
                    )
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

            val placeId =
                backStackEntry.arguments?.getString("placeId")

            if (placeId != null) {

                val newReviewViewModel: NewReviewViewModel = viewModel()

                NewReviewScreen(
                    newReviewViewModel = newReviewViewModel,
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
