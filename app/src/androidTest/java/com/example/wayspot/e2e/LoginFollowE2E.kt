
package com.example.wayspot.e2e

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.wayspot.MainActivity
import com.example.wayspot.data.datasource.AuthRemoteDataSource
import com.example.wayspot.data.repository.AuthRepository
import com.example.wayspot.data.dto.UserFirestoreDto
import com.example.wayspot.data.dto.PlaceFirestoreDto
import com.example.wayspot.data.dto.ReviewFirestoreDto
import com.example.wayspot.data.dto.ReviewUserDto
import com.google.common.truth.Truth.assertThat
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.UUID

@HiltAndroidTest
class LoginFollowE2E {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule =
        createAndroidComposeRule<MainActivity>()

    private val testEmail = "login_follow_${UUID.randomUUID()}@prueba.com"
    private val testPassword = "123456"
    private var testUserId: String? = null

    private val followedUserId = "e2e_follow_user_b"
    private val followedPlaceId = "e2e_follow_place_1"
    private val followedReviewId = "e2e_follow_review_1"
    private var followedUserCreated = false

    @Before
    fun setUp() = runTest {
        hiltRule.inject()

        Firebase.auth.signOut()

        val authRepository = AuthRepository(
            AuthRemoteDataSource(Firebase.auth)
        )

        // Registrar la cuenta de prueba
        val result = authRepository.signUp(
            testEmail,
            testPassword
        )

        assertThat(result.isSuccess).isTrue()

        val user = Firebase.auth.currentUser
        assertThat(user).isNotNull()

        testUserId = user!!.uid

        // Crear su perfil en Firestore
        val userDto = UserFirestoreDto(
            id = user.uid,
            name = "Usuario Login E2E",
            username = "login_e2e"
        )

        val db = Firebase.firestore

        // Referencia al perfil del usuario B
        val followedUserRef = db.collection("users")
            .document(followedUserId)

        // Evitar sobrescribir un perfil existente
        assertThat(followedUserRef.get().await().exists()).isFalse()

        // Crear el perfil del usuario que recibirá el follow
        val followedUser = UserFirestoreDto(
            id = followedUserId,
            name = "Usuario Seguido E2E",
            username = "seguido_e2e"
        )


        val placeRef = db.collection("places")
            .document(followedPlaceId)

        val reviewRef = db.collection("reviews")
            .document(followedReviewId)

        // Evitar sobrescribir documentos existentes
        assertThat(placeRef.get().await().exists()).isFalse()
        assertThat(reviewRef.get().await().exists()).isFalse()

        val place = PlaceFirestoreDto(
            id = followedPlaceId,
            title = "Lugar del usuario seguido E2E",
            category = "Naturaleza",
            location = "Bogotá, Colombia",
            rating = 4.5,
            description = "Lugar creado para probar el filtro",
            reviewCount = 1L
        )

        val review = ReviewFirestoreDto(
            id = followedReviewId,
            rating = 5,
            title = "Publicación del usuario seguido",
            description = "Reseña para comprobar el filtro de seguidos",
            userId = followedUserId,
            placeId = followedPlaceId,
            authorName = "Usuario Seguido E2E",
            user = ReviewUserDto(
                name = "Usuario Seguido E2E",
                username = "seguido_e2e"
            ),
            likesCount = 0
        )

        val batch = db.batch()

        // Perfil del usuario A
        batch.set(
            db.collection("users").document(user.uid),
            userDto
        )

        // Perfil del usuario B
        batch.set(followedUserRef, followedUser)

        // Publicación y reseña del usuario B
        batch.set(placeRef, place)
        batch.set(reviewRef, review)

        // Confirmar una sola vez
        batch.commit().await()

        followedUserCreated = true

        // Cerrar sesión para probar el login desde la interfaz
        Firebase.auth.signOut()
    }


    @After
    fun tearDown() = runTest {

        // Cerrar Home y sus listeners antes de eliminar los datos de prueba.
        composeRule.activityRule.scenario.close()

        val uid = testUserId
        val db = Firebase.firestore

        // Limpiar los datos del usuario B
        if (followedUserCreated) {

            // Eliminar relaciones de seguimiento
            if (uid != null) {

                db.collection("users")
                    .document(uid)
                    .collection("following")
                    .document(followedUserId)
                    .delete()
                    .await()

                db.collection("users")
                    .document(followedUserId)
                    .collection("followers")
                    .document(uid)
                    .delete()
                    .await()
            }

            // Eliminar la reseña
            db.collection("reviews")
                .document(followedReviewId)
                .delete()
                .await()

            // Eliminar el lugar
            db.collection("places")
                .document(followedPlaceId)
                .delete()
                .await()

            // Eliminar el perfil B
            db.collection("users")
                .document(followedUserId)
                .delete()
                .await()
        }

        // Limpiar el usuario A
        if (uid != null) {

            db.collection("users")
                .document(uid)
                .delete()
                .await()

            val authRepository = AuthRepository(
                AuthRemoteDataSource(Firebase.auth)
            )

            if (Firebase.auth.currentUser?.uid != uid) {
                val result = authRepository.signIn(
                    testEmail,
                    testPassword
                )

                assertThat(result.isSuccess).isTrue()
            }

            val user = Firebase.auth.currentUser

            if (user?.uid == uid) {
                user.delete().await()
            }
        }

        Firebase.auth.signOut()
    }

    @Test
    fun login_registeredUser_navigatesHome() {

        // Entrar a la pantalla de Login
        composeRule.onNodeWithTag("startLoginButton")
            .performClick()

        // Verificar que aparece Login
        composeRule.onNodeWithTag("loginScreen")
            .assertIsDisplayed()

        // Escribir las credenciales del usuario registrado
        composeRule.onNodeWithTag("Correo Electrónico")
            .performTextInput(testEmail)

        composeRule.onNodeWithTag("Contraseña")
            .performTextInput(testPassword)

        // Iniciar sesión
        composeRule.onNodeWithTag("loginSubmitButton")
            .performClick()

        // Esperar a que aparezca Home
        composeRule.waitUntil(timeoutMillis = 10000) {
            composeRule.onAllNodesWithTag("homeScreen")
                .fetchSemanticsNodes().isNotEmpty()
        }

        // Verificar que el usuario ingresó
        composeRule.onNodeWithTag("homeScreen")
            .assertIsDisplayed()


        // Esperar a que aparezca la publicación del usuario B
        composeRule.waitUntil(timeoutMillis = 10000) {
            composeRule.onAllNodesWithTag(
                "homeReviewAuthor_e2e_follow_user_b"
            ).fetchSemanticsNodes().isNotEmpty()
        }

        // Abrir el perfil del autor
        composeRule.onNodeWithTag(
            "homeReviewAuthor_e2e_follow_user_b"
        ).performClick()

        // Esperar a que se cargue el perfil
        composeRule.waitUntil(timeoutMillis = 10000) {
            composeRule.onAllNodesWithTag("profileName")
                .fetchSemanticsNodes().isNotEmpty()
        }

        // Verificar el nombre del usuario B
        composeRule.onNodeWithTag("profileName")
            .assertIsDisplayed()
            .assertTextContains("Usuario Seguido E2E")

        // Verificar su nombre de usuario
        composeRule.onNodeWithTag("profileUsername")
            .assertIsDisplayed()
            .assertTextContains("seguido_e2e", substring = true)

        // Verificar que el usuario B comienza con cero seguidores
        composeRule.waitUntil(timeoutMillis = 10000) {
            composeRule.onAllNodesWithTag("profileFollowersCount_0")
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithTag("profileFollowersCount_0")
            .assertIsDisplayed()

        // Verificar que el perfil permite seguir a este usuario
        composeRule.waitUntil(timeoutMillis = 10000) {
            composeRule.onAllNodesWithTag("profileFollowButton")
                .fetchSemanticsNodes().isNotEmpty()
        }

        // Verificar que el botón Seguir esté visible
        composeRule.onNodeWithTag("profileFollowButton")
            .assertIsDisplayed()

        // Seguir al usuario B
        composeRule.onNodeWithTag("profileFollowButton")
            .performClick()

        // Esperar a que el contador de seguidores aumente a 1
        composeRule.waitUntil(timeoutMillis = 10000) {
            composeRule.onAllNodesWithTag("profileFollowersCount_1")
                .fetchSemanticsNodes().isNotEmpty()
        }

        // Verificar que el usuario B tiene un seguidor
        composeRule.onNodeWithTag("profileFollowersCount_1")
            .assertIsDisplayed()


        // Regresar desde el perfil B a Home
        composeRule.onNodeWithTag("profileBackButton")
            .performClick()

        // Esperar a que aparezca Home nuevamente
        composeRule.waitUntil(timeoutMillis = 10000) {
            composeRule.onAllNodesWithTag("homeScreen")
                .fetchSemanticsNodes().isNotEmpty()
        }

        // Verificar que regresamos correctamente
        composeRule.onNodeWithTag("homeScreen")
            .assertIsDisplayed()

        // Comprobar que aparece el filtro desactivado
        composeRule.waitUntil(timeoutMillis = 10000) {
            composeRule.onAllNodesWithTag("homeFollowingFilter_false")
                .fetchSemanticsNodes().isNotEmpty()
        }

        // Activar el filtro de publicaciones de seguidos
        composeRule.onNodeWithTag("homeFollowingFilter_false")
            .performClick()

        // Esperar a que el filtro cambie a activado
        composeRule.waitUntil(timeoutMillis = 10000) {
            composeRule.onAllNodesWithTag("homeFollowingFilter_true")
                .fetchSemanticsNodes().isNotEmpty()
        }

        // Verificar que el filtro está activado
        composeRule.onNodeWithTag("homeFollowingFilter_true")
            .assertIsDisplayed()

        // Esperar a que aparezca una publicación del usuario B
        composeRule.waitUntil(timeoutMillis = 10000) {
            composeRule.onAllNodesWithTag(
                "homeReviewAuthor_e2e_follow_user_b"
            ).fetchSemanticsNodes().isNotEmpty()
        }

        // Verificar que aparece una publicación del usuario seguido
        composeRule.onNodeWithTag(
            "homeReviewAuthor_e2e_follow_user_b"
        ).assertIsDisplayed()



        /*
                // Activar el filtro de publicaciones de seguidos
                composeRule.onNodeWithTag("homeFollowingFilter_false")
                    .performClick()

                // Esperar a que el filtro se active
                composeRule.waitUntil(timeoutMillis = 10000) {
                    composeRule.onAllNodesWithTag("homeFollowingFilter_true")
                        .fetchSemanticsNodes().isNotEmpty()
                }

                // Esperar la publicación del usuario B
                composeRule.waitUntil(timeoutMillis = 10000) {
                    composeRule.onAllNodesWithTag(
                        "homeReviewAuthor_e2e_follow_user_b"
                    ).fetchSemanticsNodes().isNotEmpty()
                }

                // Verificar que aparece una publicación del usuario seguido
                composeRule.onNodeWithTag(
                    "homeReviewAuthor_e2e_follow_user_b"
                ).assertIsDisplayed()
        */
    }

}

