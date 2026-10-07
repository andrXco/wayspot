package com.example.wayspot

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.graphics.Color as AndroidColor
import android.os.Build
import android.view.animation.DecelerateInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.splashscreen.SplashScreen
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.wayspot.navigation.AppNavigation
import com.example.wayspot.navigation.Screen
import com.example.wayspot.ui.components.WayspotAddReviewButton
import com.example.wayspot.ui.components.WayspotBottomBar
import com.example.wayspot.ui.theme.WayspotTheme

/** Configura edge-to-edge y delega el árbol Compose a la raíz única de la aplicación. */
internal fun ComponentActivity.setWaySpotContent() {
    enableEdgeToEdge()
    setContent {
        WaySpotApp()
    }
}

/** Aplica una salida breve al splash del sistema antes de liberar la vista subyacente. */
internal fun SplashScreen.configureWaySpotExitAnimation() {
    setOnExitAnimationListener { splashScreenView ->
        splashScreenView.view
            .animate()
            .alpha(0f)
            .setDuration(SYSTEM_SPLASH_EXIT_DURATION_MS)
            .setInterpolator(DecelerateInterpolator())
            .withEndAction {
                splashScreenView.remove()
            }
            .start()
    }
}

/**
 * Raíz visual de WaySpot.
 *
 * Es propietaria del `NavController`, del único `Scaffold`, de las barras del sistema y de la
 * política centralizada de insets para las rutas que se dibujan borde a borde.
 */
@Composable
fun WaySpotApp(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var localNetworkPermissionHandled by remember {
        mutableStateOf(
            Build.VERSION.SDK_INT < 37 ||
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_LOCAL_NETWORK
                ) == PackageManager.PERMISSION_GRANTED
        )
    }
    val localNetworkPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        localNetworkPermissionHandled = true
    }

    LaunchedEffect(Unit) {
        if (!localNetworkPermissionHandled) {
            localNetworkPermissionLauncher.launch(Manifest.permission.ACCESS_LOCAL_NETWORK)
        }
    }

    if (!localNetworkPermissionHandled) return

    WayspotTheme {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val drawsBehindStatusBar = currentRoute in edgeToEdgeRoutes
        val drawsBehindNavigationBar = currentRoute == Screen.Splash.route
        val useDarkStatusBarIcons = when (currentRoute) {
            Screen.Splash.route -> false
            Screen.PlaceDetail.route -> false
            Screen.Profile.route -> MaterialTheme.colorScheme.primary.luminance() > 0.5f
            else -> MaterialTheme.colorScheme.background.luminance() > 0.5f
        }
        val useDarkNavigationBarIcons = when (currentRoute) {
            Screen.Splash.route -> false
            else -> MaterialTheme.colorScheme.background.luminance() > 0.5f
        }
        val view = LocalView.current

        SideEffect {
            view.context.findActivity()?.let { activity ->
                val window = activity.window
                val insetsController = WindowCompat.getInsetsController(
                    window,
                    view
                )
                insetsController.isAppearanceLightStatusBars = useDarkStatusBarIcons
                insetsController.isAppearanceLightNavigationBars = useDarkNavigationBarIcons
                makeNavigationBarTransparent(activity)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    window.isNavigationBarContrastEnforced = false
                }
            }
        }

        Scaffold(
            modifier = modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.background,
            contentWindowInsets = if (drawsBehindNavigationBar) {
                WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal)
            } else {
                WindowInsets.safeDrawing.only(
                    WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom
                )
            },
            bottomBar = {
                if (currentRoute != null && currentRoute in bottomBarRoutes) {
                    WayspotBottomBar(
                        currentRoute = currentRoute,
                        onNavItemClick = { route ->
                            navController.navigate(route)
                        }
                    )
                }
            },
            floatingActionButton = {
                if (currentRoute != null && currentRoute in bottomBarRoutes) {
                    WayspotAddReviewButton(
                        onClick = {
                            navController.navigate(
                                Screen.NewReview.createRoute()
                            )
                        }
                    )
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                AppNavigation(
                    navController = navController,
                    modifier = if (drawsBehindStatusBar) {
                        Modifier
                    } else {
                        Modifier.statusBarsPadding()
                    }
                )
            }
        }
    }
}

private val bottomBarRoutes = setOf(
    Screen.Home.route,
    Screen.Explore.route,
    Screen.Notifications.route,
    Screen.Profile.route
)

private val edgeToEdgeRoutes = setOf(
    Screen.Splash.route,
    Screen.PlaceDetail.route,
    Screen.Profile.route
)

private const val SYSTEM_SPLASH_EXIT_DURATION_MS = 450L

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

@Suppress("DEPRECATION")
private fun makeNavigationBarTransparent(activity: Activity) {
    activity.window.navigationBarColor = AndroidColor.TRANSPARENT
}
