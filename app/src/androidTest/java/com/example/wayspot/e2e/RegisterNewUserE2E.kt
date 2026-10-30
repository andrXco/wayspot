package com.example.wayspot.e2e

import android.os.Build
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.core.app.ActivityScenario
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.isToggleable
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import com.example.wayspot.MainActivity
import com.example.wayspot.R
import com.google.common.truth.Truth.assertThat
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RegisterNewUserE2E {
    @get:Rule
    val composeRule = createEmptyComposeRule()
    private lateinit var activity: ActivityScenario<MainActivity>
    private val context = androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setUp() {
        if (Build.VERSION.SDK_INT >= 37) {
            val instrumentation = InstrumentationRegistry.getInstrumentation()
            instrumentation.uiAutomation.grantRuntimePermission(
                instrumentation.targetContext.packageName,
                "android.permission.ACCESS_LOCAL_NETWORK"
            )
        }

        try {
            Firebase.auth.useEmulator("10.0.2.2", 9099)
            Firebase.firestore.useEmulator("10.0.2.2", 8080)
        } catch (e: Exception) {
        }
        Firebase.auth.signOut()
        activity = ActivityScenario.launch(MainActivity::class.java)
    }

    // Toca iniciar sesión en la app y comprueba que aparece el formulario de acceso.
    @Test
    fun navigate_fromStart_toLogin() {
        composeRule.onNodeWithText(context.getString(R.string.welcome_login_button)).performClick()
        composeRule.onNodeWithText(context.getString(R.string.label_email)).assertIsDisplayed()
    }

    // Toca crear cuenta y comprueba que aparece el formulario de registro.
    @Test
    fun navigate_fromStart_toRegister() {
        composeRule.onNodeWithText(context.getString(R.string.welcome_signup_button)).performClick()
        composeRule.onNodeWithText(context.getString(R.string.label_full_name)).assertIsDisplayed()
    }

    // Completa el formulario con contraseña corta y verifica el error sin crear una cuenta.
    @Test
    fun registerUser_shortPassword_showsError() {
        composeRule.onNodeWithText(context.getString(R.string.welcome_signup_button)).performClick()
        composeRule.onAllNodes(hasSetTextAction())[0].performScrollTo().performTextInput("Usuario E2E")
        composeRule.onAllNodes(hasSetTextAction())[1].performScrollTo().performTextInput("usuario_e2e")
        composeRule.onAllNodes(hasSetTextAction())[2].performScrollTo().performTextInput("registro_e2e@prueba.com")
        composeRule.onAllNodes(hasSetTextAction())[3].performScrollTo().performTextInput("12345")
        composeRule.onAllNodes(hasSetTextAction())[4].performScrollTo().performTextInput("12345")
        composeRule.onNode(isToggleable()).performScrollTo().performClick()
        composeRule.onNodeWithText(context.getString(R.string.welcome_signup_button))
            .performScrollTo().performClick()

        composeRule.waitUntil(10000) {
            composeRule.onAllNodesWithText(context.getString(R.string.auth_signup_error_title))
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText("La contraseña debe tener al menos 6 caracteres").assertIsDisplayed()
        assertThat(Firebase.auth.currentUser).isNull()
    }

    // Registra una cuenta desde la interfaz y verifica la navegación y el perfil guardado en Firestore.
    @Test
    fun registerUser_validInputs_createsProfileAndNavigatesHome() = runTest {
        composeRule.onNodeWithText(context.getString(R.string.welcome_signup_button)).performClick()
        composeRule.onAllNodes(hasSetTextAction())[0].performScrollTo().performTextInput("Usuario E2E")
        composeRule.onAllNodes(hasSetTextAction())[1].performScrollTo().performTextInput("usuario_e2e")
        composeRule.onAllNodes(hasSetTextAction())[2].performScrollTo().performTextInput("registro_e2e@prueba.com")
        composeRule.onAllNodes(hasSetTextAction())[3].performScrollTo().performTextInput("TestPassword123!")
        composeRule.onAllNodes(hasSetTextAction())[4].performScrollTo().performTextInput("TestPassword123!")
        composeRule.onNode(isToggleable()).performScrollTo().performClick()
        composeRule.onNodeWithText(context.getString(R.string.welcome_signup_button))
            .performScrollTo().performClick()

        composeRule.waitUntil(30000) {
            composeRule.onAllNodesWithContentDescription(context.getString(R.string.nav_profile))
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithContentDescription(context.getString(R.string.nav_profile)).assertIsDisplayed()
        val user = Firebase.auth.currentUser
        assertThat(user).isNotNull()
        val profile = Firebase.firestore.collection("users").document(user!!.uid).get().await()
        assertThat(profile.getString("username")).isEqualTo("usuario_e2e")
        assertThat(profile.getString("name")).isEqualTo("Usuario E2E")
    }

    @After
    fun tearDown() = runTest {
        if (::activity.isInitialized) activity.close()
        val user = Firebase.auth.currentUser
        if (user != null) {
            Firebase.firestore.collection("users").document(user.uid).delete().await()
            user.delete().await()
        }
        Firebase.auth.signOut()
    }
}
