package com.example.wayspot.e2e

import android.os.Build
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.core.app.ActivityScenario
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import com.example.wayspot.MainActivity
import com.example.wayspot.R
import com.example.wayspot.data.dto.UserFirestoreDto
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

class ModifyUserE2E {
    @get:Rule
    val composeRule = createEmptyComposeRule()
    private lateinit var activity: ActivityScenario<MainActivity>
    private val context = androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setUp() = runTest {
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
        val result = Firebase.auth.createUserWithEmailAndPassword("perfil_e2e@prueba.com", "TestPassword123!").await()
        val userId = result.user!!.uid
        Firebase.firestore.collection("users").document(userId).set(
            UserFirestoreDto(id = userId, name = "Usuario E2E", username = "usuario_e2e", country = "Colombia")
        ).await()
        activity = ActivityScenario.launch(MainActivity::class.java)
    }

    // Cambia el nombre de usuario desde la interfaz y comprueba que queda guardado y se muestra al volver al perfil.
    @Test
    fun updateUserInfo_persistsUsernameAndShowsItWhenReopeningProfile() = runTest {
        composeRule.waitUntil(15000) {
            composeRule.onAllNodesWithContentDescription(context.getString(R.string.nav_profile))
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithContentDescription(context.getString(R.string.nav_profile)).performClick()
        composeRule.waitUntil(10000) {
            composeRule.onAllNodesWithText("@usuario_e2e").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText(context.getString(R.string.profile_button_edit)).performScrollTo().performClick()
        composeRule.waitUntil(10000) {
            composeRule.onAllNodes(hasSetTextAction()).fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onAllNodes(hasSetTextAction())[0].performScrollTo().performTextClearance()
        composeRule.onAllNodes(hasSetTextAction())[0].performTextInput("modificado_e2e")
        composeRule.onNodeWithContentDescription(context.getString(R.string.edit_profile_save_changes_content_description))
            .performClick()

        composeRule.waitUntil(30000) {
            composeRule.onAllNodesWithText("@modificado_e2e").fetchSemanticsNodes().isNotEmpty()
        }
        val userId = Firebase.auth.currentUser!!.uid
        val profile = Firebase.firestore.collection("users").document(userId).get().await()
        assertThat(profile.getString("username")).isEqualTo("modificado_e2e")

        composeRule.onNodeWithContentDescription(context.getString(R.string.nav_home)).performClick()
        composeRule.onNodeWithContentDescription(context.getString(R.string.nav_profile)).performClick()
        composeRule.onNodeWithText("@modificado_e2e").assertIsDisplayed()
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
