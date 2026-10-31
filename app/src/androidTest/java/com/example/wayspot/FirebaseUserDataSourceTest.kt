package com.example.wayspot

import android.os.Build
import androidx.test.platform.app.InstrumentationRegistry
import com.example.wayspot.data.datasource.implementation.firestore.UserFirestoreDataSourceImplementation
import com.example.wayspot.data.dto.RegisterUserDto
import com.example.wayspot.data.dto.ReviewFirestoreDto
import com.example.wayspot.data.dto.UserFirestoreDto
import com.google.common.truth.Truth.assertThat
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.tasks.await
import org.junit.After
import org.junit.Before
import org.junit.Test

class FirebaseUserDataSourceTest {
    private val db = Firebase.firestore
    private lateinit var dataSource: UserFirestoreDataSourceImplementation

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
            db.useEmulator("10.0.2.2", 8080)
        } catch (e: Exception) {
        }

        Firebase.auth.signOut()

        val users = db.collection("users").get().await()
        for (userDoc in users) {
            val followers = userDoc.reference.collection("followers").get().await()
            for (follower in followers) {
                follower.reference.delete().await()
            }
            val following = userDoc.reference.collection("following").get().await()
            for (followed in following) {
                followed.reference.delete().await()
            }
            userDoc.reference.delete().await()
        }
        val reviews = db.collection("reviews").get().await()
        for (review in reviews) {
            review.reference.delete().await()
        }

        repeat(10) { index ->
            val user = generateUser(index)
            db.collection("users").document(user.id).set(user).await()
        }
        dataSource = UserFirestoreDataSourceImplementation(db)
    }

    // Consulta la colección y comprueba que devuelve los diez usuarios creados en el setup.
    @Test
    fun getUsers_returnsSeededUsers() = runTest {
        val result = dataSource.getUsers()

        assertThat(result).hasSize(10)
        assertThat(result.map { it.id }).contains("user_9")
    }

    // Busca un usuario existente y verifica que Firestore devuelve sus datos y su ID.
    @Test
    fun getUserById_validate_correctUser() = runTest {
        val id = "user_9"

        val result = dataSource.getUserById(id)

        assertThat(result).isNotNull()
        assertThat(result?.name).isEqualTo("Name 9")
        assertThat(result?.id).isEqualTo(id)
    }

    // Busca un ID que no existe y comprueba que el data source devuelve null.
    @Test
    fun getUserById_validate_missingUser() = runTest {
        val result = dataSource.getUserById("user_999")

        assertThat(result).isNull()
    }

    // Registra un usuario y comprueba que queda guardado con el ID indicado.
    @Test
    fun registerUser_insert_Document_DocumentExists() = runTest {
        val user = RegisterUserDto(
            username = "username",
            name = "name",
            country = "pais",
            bio = "Bio de prueba",
            fcmToken = ""
        )

        dataSource.registerUser(user, "user_999")
        val result = dataSource.getUserById("user_999")

        assertThat(result?.name).isEqualTo("name")
        assertThat(result?.id).isEqualTo("user_999")
    }

    // Sigue a otro usuario y comprueba que la relación queda creada.
    @Test
    fun followOrUnfollowUser_followUser_UserFollowed() = runTest {
        dataSource.followOrUnfollowUser("user_1", "user_2")

        assertThat(dataSource.isFollowing("user_1", "user_2")).isTrue()
    }

    // Sigue a otro usuario y comprueba que aumenta en uno su contador de seguidores.
    @Test
    fun followOrUnfollowUser_followUser_followersCountIncrement() = runTest {
        val oldCount = dataSource.getFollowersCount("user_2")

        dataSource.followOrUnfollowUser("user_1", "user_2")
        val newCount = dataSource.getFollowersCount("user_2")

        assertThat(newCount).isGreaterThan(oldCount)
    }

    // Sigue y deja de seguir al mismo usuario; comprueba que la relación termina eliminada.
    @Test
    fun followOrUnfollowUser_unfollowUser_followedFalse() = runTest {
        dataSource.followOrUnfollowUser("user_1", "user_2")
        dataSource.followOrUnfollowUser("user_1", "user_2")

        assertThat(dataSource.isFollowing("user_1", "user_2")).isFalse()
    }

    // Guarda una reseña y comprueba que la consulta devuelve la reseña de ese usuario con su ID.
    @Test
    fun getUserReviews_valid_correctReviews() = runTest {
        val userId = "user_1"
        db.collection("reviews").document("test_review").set(
            mapOf(
                "rating" to 5,
                "title" to "Reseña de prueba",
                "description" to "Descripción de prueba",
                "userId" to userId,
                "placeId" to "test_place"
            )
        ).await()

        val result = dataSource.getReviewsByUserId(userId)
        val review = result.single() as ReviewFirestoreDto

        assertThat(review.id).isEqualTo("test_review")
        assertThat(review.description).isEqualTo("Descripción de prueba")
    }

    private fun generateUser(index: Int) = UserFirestoreDto(
        id = "user_$index",
        name = "Name $index",
        username = "username_$index",
        country = "Colombia",
        bio = "Bio $index"
    )

    @After
    fun tearDown() = runTest {
        val users = db.collection("users").get().await()
        for (userDoc in users) {
            val followers = userDoc.reference.collection("followers").get().await()
            for (follower in followers) {
                follower.reference.delete().await()
            }
            val following = userDoc.reference.collection("following").get().await()
            for (followed in following) {
                followed.reference.delete().await()
            }
            userDoc.reference.delete().await()
        }
        val reviews = db.collection("reviews").get().await()
        for (review in reviews) {
            review.reference.delete().await()
        }
    }
}
