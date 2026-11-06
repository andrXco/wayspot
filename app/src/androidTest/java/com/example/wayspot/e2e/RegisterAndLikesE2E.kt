
package com.example.wayspot.e2e

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.onNodeWithContentDescription
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import com.example.wayspot.MainActivity
import com.example.wayspot.data.dto.PlaceFirestoreDto
import com.example.wayspot.data.dto.ReviewFirestoreDto
import com.example.wayspot.data.dto.ReviewUserDto
import com.example.wayspot.data.dto.UserFirestoreDto
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.common.truth.Truth.assertThat
import java.util.UUID



@HiltAndroidTest
class RegisterAndLikesE2E {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule =
        createAndroidComposeRule<MainActivity>()

    private var testDataCreated = false
    private val testEmail = "register_likes_${UUID.randomUUID()}@prueba.com"
    private var placeCount = 0


    @Before
    fun setUp() = runTest {
        hiltRule.inject()

        Firebase.auth.signOut()

        val db = Firebase.firestore

        val placeRef = db.collection("places")
            .document("e2e_place_1")

        val reviewRef = db.collection("reviews")
            .document("e2e_review_1")

        val authorRef = db.collection("users")
            .document("e2e_author_1")

        assertThat(authorRef.get().await().exists()).isFalse()

        // Verificar que no vamos a sobrescribir documentos
        assertThat(placeRef.get().await().exists()).isFalse()
        assertThat(reviewRef.get().await().exists()).isFalse()

        // Preparar una publicación conocida
        val place = PlaceFirestoreDto(
            id = "e2e_place_1",
            title = "Lugar de prueba E2E",
            category = "Naturaleza",
            location = "Bogotá, Colombia",
            rating = 4.5,
            description = "Lugar creado para la prueba E2E",
            reviewCount = 1L
        )

        // Preparar una reseña vinculada a la publicación
        val review = ReviewFirestoreDto(
            id = "e2e_review_1",
            rating = 5,
            title = "Reseña de prueba",
            description = "Comentario para probar likes",
            userId = "e2e_author_1",
            placeId = "e2e_place_1",
            authorName = "Autor E2E",
            user = ReviewUserDto(
                name = "Autor E2E",
                username = "autor_e2e"
            ),
            likesCount = 0
        )

        val batch = db.batch()

        val author = UserFirestoreDto(
            id = "e2e_author_1",
            name = "Autor E2E",
            username = "autor_e2e"
        )

        batch.set(authorRef, author)
        batch.set(placeRef, place)
        batch.set(reviewRef, review)

        batch.commit().await()
        assertThat(placeRef.get().await().exists()).isTrue()
        placeCount = db.collection("places").get().await().documents.size

        testDataCreated = true
    }

    @Test
    fun navigate_fromStart_toRegister() {

        // Entrar al formulario de registro
        composeRule
            .onNodeWithTag("startRegisterButton")
            .performClick()

        // Verificar que se muestra el formulario
        composeRule
            .onNodeWithTag("registerScreen")
            .assertIsDisplayed()
    }

    @Test
    fun register_shortPassword_showsError() {

        // Entrar al formulario de registro
        composeRule.onNodeWithTag("startRegisterButton")
            .performClick()

        composeRule.onNodeWithTag("registerScreen")
            .assertIsDisplayed()

        // Completar los datos del usuario
        composeRule.onNodeWithTag("Nombre Completo")
            .performTextInput("Usuario E2E")

        composeRule.onNodeWithTag("Nombre de usuario")
            .performTextInput("usuario_e2e")

        composeRule.onNodeWithTag("Correo Electrónico")
            .performTextInput(testEmail)

        composeRule.onNodeWithTag("Contraseña")
            .performTextInput("1234")

        composeRule.onNodeWithTag("Confirmar contraseña")
            .performTextInput("1234")
        composeRule.onNodeWithTag("Confirmar contraseña")
            .performImeAction()

        // Aceptar términos y enviar el formulario
        composeRule.onNodeWithTag("registerTermsCheckbox")
            .performScrollTo()
            .performClick()
        composeRule.onNodeWithTag("registerTermsCheckbox")
            .assertIsOn()

        composeRule.onNodeWithTag("registerSubmitButton")
            .performScrollTo()
            .assertIsEnabled()
        composeRule.onNodeWithTag("registerSubmitButton")
            .performClick()

        // Esperar a que aparezca el error
        composeRule.waitUntil(timeoutMillis = 10000) {
            composeRule.onAllNodesWithTag("registerErrorMessage")
                .fetchSemanticsNodes().isNotEmpty()
        }

        // Verificar que el mensaje esté visible
        composeRule.onNodeWithTag("registerErrorMessage")
            .assertIsDisplayed()
            .assertTextContains("La contraseña debe tener al menos 6 caracteres")

        // Cerrar el mensaje de error
        composeRule.onNodeWithTag("registerDismissErrorButton")
            .performClick()

        // Corregir la contraseña
        composeRule.onNodeWithTag("Contraseña")
            .performTextClearance()

        composeRule.onNodeWithTag("Contraseña")
            .performTextInput("123456")

        // Corregir la confirmación
        composeRule.onNodeWithTag("Confirmar contraseña")
            .performTextClearance()

        composeRule.onNodeWithTag("Confirmar contraseña")
            .performTextInput("123456")
        composeRule.onNodeWithTag("Confirmar contraseña")
            .performImeAction()


        // Registrar al usuario con la contraseña corregida
        composeRule.onNodeWithTag("registerSubmitButton")
            .performScrollTo()
            .assertIsEnabled()
        composeRule.onNodeWithTag("registerSubmitButton")
            .performClick()

        // Esperar a que finalice el registro y aparezca Home
        composeRule.waitUntil(timeoutMillis = 10000) {
            composeRule.onAllNodesWithTag("homeScreen")
                .fetchSemanticsNodes().isNotEmpty()
        }

        // Verificar que el usuario ingresó a Home
        composeRule.onNodeWithTag("homeScreen")
            .assertIsDisplayed()


        // Esperar a que aparezca la publicación de prueba
        composeRule.onNodeWithTag("homeScreen")
            .performScrollToIndex(2)

        val placeTag = "e2ePlace_e2e_place_1"
        for (index in 0 until placeCount) {
            if (composeRule.onAllNodesWithTag(placeTag)
                    .fetchSemanticsNodes().isNotEmpty()
            ) {
                break
            }
            composeRule.onNodeWithContentDescription("Siguiente plan")
                .performClick()
        }

        // Abrir la publicación desde Home
        composeRule.onNodeWithTag(placeTag)
            .assertIsDisplayed()
            .performClick()

        // Esperar a que cargue el detalle
        composeRule.waitUntil(timeoutMillis = 10000) {
            composeRule.onAllNodesWithTag("placeDetailTitle")
                .fetchSemanticsNodes().isNotEmpty()
        }

        // Verificar el título de la publicación
        composeRule.onNodeWithTag("placeDetailTitle")
            .assertIsDisplayed()
            .assertTextContains("Lugar de prueba E2E")

        // Verificar la ubicación

        composeRule.onNodeWithTag("placeDetailLocation")
            .assertIsDisplayed()
            .assertTextContains("Bogotá, Colombia", substring = true)

        // Esperar a que aparezca la reseña de prueba
        composeRule.waitUntil(timeoutMillis = 10000) {
            composeRule.onAllNodesWithTag("e2eReview_e2e_review_1")
                .fetchSemanticsNodes().isNotEmpty()
        }

        // Abrir el detalle de la reseña
        composeRule.onNodeWithTag("e2eReview_e2e_review_1")
            .performClick()

        // Esperar a que aparezca el contador de likes
        composeRule.waitUntil(timeoutMillis = 10000) {
            composeRule.onAllNodesWithTag("reviewLikesCount")
                .fetchSemanticsNodes().isNotEmpty()
        }

        // Verificar que inicialmente tiene cero likes
        composeRule.onNodeWithTag("reviewLikesCount")
            .assertIsDisplayed()
            .assertTextContains("0")

        // Dar like
        composeRule.onNodeWithTag("reviewLikeButton")
            .performClick()

        // Esperar a que el estado muestre un like
        composeRule.waitUntil(timeoutMillis = 10000) {
            composeRule.onAllNodesWithTag("reviewLikesState_1")
                .fetchSemanticsNodes().isNotEmpty()
        }

        // Comprobar el contador
        composeRule.onNodeWithTag("reviewLikesCount")
            .assertIsDisplayed()
            .assertTextContains("1")


        // Regresar desde la reseña al detalle del lugar
        composeRule.onNodeWithTag("reviewBackButton")
            .performClick()

        composeRule.waitUntil(timeoutMillis = 10000) {
            composeRule.onAllNodesWithTag("placeDetailTitle")
                .fetchSemanticsNodes().isNotEmpty()
        }

        // Regresar al Home
        composeRule.onNodeWithContentDescription("Volver")
            .performClick()

        composeRule.waitUntil(timeoutMillis = 10000) {
            composeRule.onAllNodesWithTag("homeScreen")
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithTag("homeScreen")
            .performScrollToIndex(2)
        for (index in 0 until placeCount) {
            if (composeRule.onAllNodesWithTag(placeTag)
                    .fetchSemanticsNodes().isNotEmpty()
            ) {
                break
            }
            composeRule.onNodeWithContentDescription("Siguiente plan")
                .performClick()
        }

        // Volver a seleccionar la publicación
        composeRule.onNodeWithTag("e2ePlace_e2e_place_1")
            .assertIsDisplayed()
            .performClick()

        composeRule.waitUntil(timeoutMillis = 10000) {
            composeRule.onAllNodesWithTag("e2eReview_e2e_review_1")
                .fetchSemanticsNodes().isNotEmpty()
        }

        // Abrir nuevamente la reseña
        composeRule.onNodeWithTag("e2eReview_e2e_review_1")
            .performClick()

        // Comprobar que el like anterior se mantiene
        composeRule.waitUntil(timeoutMillis = 10000) {
            composeRule.onAllNodesWithTag("reviewLikesState_1")
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithTag("reviewLikesCount")
            .assertIsDisplayed()
            .assertTextContains("1")

        // Quitar el like
        composeRule.onNodeWithTag("reviewLikeButton")
            .performClick()

        // Esperar a que el contador disminuya
        composeRule.waitUntil(timeoutMillis = 10000) {
            composeRule.onAllNodesWithTag("reviewLikesState_0")
                .fetchSemanticsNodes().isNotEmpty()
        }

        // Verificar que el contador volvió a cero
        composeRule.onNodeWithTag("reviewLikesCount")
            .assertIsDisplayed()
            .assertTextContains("0")

    }

    @After
    fun tearDown() = runTest {

        if (testDataCreated) {
            val db = Firebase.firestore

            val reviewRef = db.collection("reviews")
                .document("e2e_review_1")

            // Eliminar posibles likes de la reseña
            val likes = reviewRef.collection("likes")
                .get().await()

            for (like in likes.documents) {
                like.reference.delete().await()
            }

            // Eliminar los documentos creados por la prueba
            reviewRef.delete().await()

            db.collection("places")
                .document("e2e_place_1")
                .delete().await()

            db.collection("users")
                .document("e2e_author_1")
                .delete()
                .await()
        }

        // Eliminar el usuario registrado durante el E2E
        val user = Firebase.auth.currentUser

        if (user != null && user.email == testEmail) {
            Firebase.firestore.collection("users")
                .document(user.uid)
                .delete().await()

            user.delete().await()
        }

        Firebase.auth.signOut()
    }

    @Test
    fun navigate_fromStart_toLogin() {

        // Entrar a la pantalla de inicio de sesión
        composeRule.onNodeWithTag("startLoginButton")
            .performClick()

        // Comprobar que aparece la pantalla Login
        composeRule.onNodeWithTag("loginScreen")
            .assertIsDisplayed()
    }


}
