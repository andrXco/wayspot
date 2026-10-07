package com.example.wayspot.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.wayspot.R
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

/**
 * Registra los destinos de la aplicación y adapta sus callbacks al `NavController`.
 *
 * También entrega a los destinos el estado compartido gestionado por
 * [AppNavigationViewModel], sin exponer el controlador de navegación a las pantallas.
 */
@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    val appNavigationViewModel: AppNavigationViewModel = hiltViewModel()

    val appNavigationState by appNavigationViewModel.uiState.collectAsState()
    val userProfile = appNavigationState.userProfile ?: return
    val savedPlaces = appNavigationState.savedPlaces

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier
    ) {

        composable(Screen.ForgotPassword.route) {

            val forgotPasswordViewModel: ForgotPasswordViewModel = hiltViewModel()

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

            val savedPlacesViewModel: SavedPlacesViewModel = hiltViewModel()

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

            val editProfileViewModel: EditProfileViewModel = hiltViewModel()

            EditProfileScreen(
                editProfileViewModel = editProfileViewModel,
                profile = userProfile,
                onAvatarUploaded = appNavigationViewModel::updateUserAvatar,

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

            val profileViewModel: ProfileViewModel = hiltViewModel()

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
                },

                onSignOut = {
                    appNavigationViewModel.resetUserProfile()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Screen.Explore.route) {

            val exploreViewModel: ExploreViewModel = hiltViewModel()

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

                val placeDetailViewModel: PlaceDetailViewModel = hiltViewModel()

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

            val splashViewModel: SplashViewModel = hiltViewModel()

            SplashScreen(
                splashViewModel = splashViewModel,
                onAuthenticated = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                onLoginClick = {
                    navController.navigate(Screen.Login.route){
                        popUpTo(0) {inclusive = true}
                    }
                },

                onSignUpClick = {
                    navController.navigate(Screen.SignUp.route){
                        popUpTo(0) {inclusive = true}

                    }
                }
            )
        }

        composable(Screen.Login.route) {

            val loginViewModel: LoginViewModel = hiltViewModel()

            LoginScreen(
                loginViewModel = loginViewModel,

                onLoginClick = {
                    appNavigationViewModel.syncAuthenticatedUser()
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

            val notificationsViewModel: NotificationsViewModel = hiltViewModel()

            NotificationsScreen(
                notificationsViewModel = notificationsViewModel
            )
        }

        composable(Screen.Home.route) {

            val homeViewModel: HomeViewModel = hiltViewModel()

            HomeScreen(
                homeViewModel = homeViewModel,
                savedPlaces = savedPlaces,
                onPlaceClick = { placeId ->
                    navController.navigate(
                        Screen.PlaceDetail.createRoute(placeId)
                    )
                },

                onSaveClick = { place ->
                    appNavigationViewModel.toggleSavedPlace(place)
                }
            )
        }

        composable(Screen.SignUp.route) {

            val signUpViewModel: SignUpViewModel = hiltViewModel()

            SignUpScreen(
                signUpViewModel = signUpViewModel,

                onSignUpClick = {
                    appNavigationViewModel.syncAuthenticatedUser()
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
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->

            val placeId =
                backStackEntry.arguments?.getString("placeId")

            val newReviewViewModel: NewReviewViewModel = hiltViewModel()

            NewReviewScreen(
                newReviewViewModel = newReviewViewModel,
                placeId = placeId,
                onBackClick = {
                    navController.popBackStack()
                },
                onPublishReview = {
                    Toast.makeText(
                        context,
                        R.string.new_review_publish_confirmation,
                        Toast.LENGTH_SHORT
                    ).show()
                    navController.popBackStack()
                }
            )
        }
    }
}
