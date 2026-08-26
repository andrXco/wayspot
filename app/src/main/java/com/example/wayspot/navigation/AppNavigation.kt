package com.example.wayspot.navigation

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.example.wayspot.data.local.PreviewData
import com.example.wayspot.data.model.Place
import com.example.wayspot.data.model.ReviewDraft
import com.example.wayspot.data.model.SavedPlacesRules
import com.example.wayspot.ui.screens.auth.login.LoginScreen
import com.example.wayspot.ui.screens.auth.signup.SignUpScreen
import com.example.wayspot.ui.screens.home.HomeScreen
import com.example.wayspot.ui.screens.editprofile.EditProfileScreen
import com.example.wayspot.ui.screens.newreview.NewReviewScreen
import com.example.wayspot.ui.screens.notifications.NotificationsScreen
import com.example.wayspot.ui.screens.placedetail.PlaceDetailScreen
import com.example.wayspot.ui.screens.splash.SplashScreen

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier
) {

    var currentRoute by remember {
        mutableStateOf(Routes.SPLASH)
    }

    var homeRoute by remember {
        mutableStateOf(Routes.HOME)
    }

    var userProfile by remember {
        mutableStateOf(PreviewData.userProfile)
    }

    var savedPlaces by remember {
        mutableStateOf(PreviewData.savedPlaces)
    }

    var selectedPlace by remember {
        mutableStateOf<Place?>(null)
    }

    var reviewDrafts by remember {
        mutableStateOf<Map<String, ReviewDraft>>(emptyMap())
    }

    var publishedReviews by remember {
        mutableStateOf<List<ReviewDraft>>(emptyList())
    }

    val profileDrawsBehindStatusBar =
        currentRoute == Routes.HOME && homeRoute == Routes.PROFILE
    val useDarkStatusBarIcons = if (profileDrawsBehindStatusBar) {
        isSystemInDarkTheme()
    } else {
        !isSystemInDarkTheme()
    }
    val view = LocalView.current

    SideEffect {
        if (!view.isInEditMode) {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                WindowCompat.getInsetsController(window, window.decorView)
                    .isAppearanceLightStatusBars = useDarkStatusBarIcons
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .then(
                if (profileDrawsBehindStatusBar) {
                    Modifier
                } else {
                    Modifier.statusBarsPadding()
                }
            )
    ) {
        when (currentRoute) {

            Routes.SPLASH -> {
                SplashScreen(
                    onLoginClick = {
                        currentRoute = Routes.LOGIN
                    },
                    onSignUpClick = {
                        currentRoute = Routes.SIGNUP
                    }
                )
            }

            Routes.LOGIN -> {
                LoginScreen(
                    onLoginClick = {
                        currentRoute = Routes.HOME
                    },
                    onSignUpClick = {
                        currentRoute = Routes.SIGNUP
                    }
                )
            }

            Routes.SIGNUP -> {
                SignUpScreen(
                    onSignUpClick = {
                        currentRoute = Routes.LOGIN
                    },
                    onBackToLoginClick = {
                        currentRoute = Routes.LOGIN
                    }
                )
            }

            Routes.HOME -> {
                HomeScreen(
                    currentRoute = homeRoute,
                    userProfile = userProfile,
                    savedPlaces = savedPlaces,

                    onNavItemClick = {
                        homeRoute = it
                    },

                    onNotificationsClick = {
                        currentRoute = Routes.NOTIFICATIONS
                    },

                    onEditProfileClick = {
                        currentRoute = Routes.EDIT_PROFILE
                    },

                    onSavedPlacesClick = {
                        homeRoute = Routes.SAVED_PLACES
                    },

                    onRemoveSavedPlace = { placeId, list ->
                        savedPlaces = SavedPlacesRules.removeFromList(
                            savedPlaces = savedPlaces,
                            placeId = placeId,
                            list = list
                        )
                    },

                    onToggleSavedPlace = { place ->
                        savedPlaces = SavedPlacesRules.toggleSaved(
                            savedPlaces = savedPlaces,
                            place = place
                        )
                    },

                    onPlaceClick = { place ->
                        selectedPlace = place
                        currentRoute = Routes.PLACE_DETAIL
                    }
                )
            }

            Routes.PLACE_DETAIL -> {
                selectedPlace?.let { place ->
                    PlaceDetailScreen(
                        place = place,
                        onBackClick = {
                            currentRoute = Routes.HOME
                        },
                        onWriteReviewClick = {
                            currentRoute = Routes.NEW_REVIEW
                        }
                    )
                }
            }

            Routes.NEW_REVIEW -> {
                selectedPlace?.let { place ->
                    NewReviewScreen(
                        place = place,
                        onBackClick = {
                            currentRoute = Routes.PLACE_DETAIL
                        },
                        initialDraft = reviewDrafts[place.id],
                        onSaveDraft = { reviewDraft ->
                            reviewDrafts = reviewDrafts +
                                (reviewDraft.placeId to reviewDraft)
                            currentRoute = Routes.PLACE_DETAIL
                        },
                        onPublishReview = { review ->
                            publishedReviews = publishedReviews + review
                            reviewDrafts = reviewDrafts - review.placeId
                            currentRoute = Routes.PLACE_DETAIL
                        }
                    )
                }
            }

            Routes.NOTIFICATIONS -> {
                NotificationsScreen(
                    onBackClick = {
                        currentRoute = Routes.HOME
                    }
                )
            }

            Routes.EDIT_PROFILE -> {
                EditProfileScreen(
                    profile = userProfile,
                    onBackClick = {
                        homeRoute = Routes.PROFILE
                        currentRoute = Routes.HOME
                    },
                    onSaveClick = { updatedProfile ->
                        userProfile = updatedProfile
                        homeRoute = Routes.PROFILE
                        currentRoute = Routes.HOME
                    },
                    onDeleteAccountConfirmed = {
                        userProfile = PreviewData.userProfile
                        savedPlaces = PreviewData.savedPlaces
                        selectedPlace = null
                        reviewDrafts = emptyMap()
                        publishedReviews = emptyList()
                        homeRoute = Routes.HOME
                        currentRoute = Routes.LOGIN
                    }
                )
            }
        }
    }
}
