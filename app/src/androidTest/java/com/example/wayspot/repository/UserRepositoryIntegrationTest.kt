package com.example.wayspot.repository

import android.os.Build
import androidx.test.platform.app.InstrumentationRegistry
import com.example.wayspot.data.datasource.implementation.firestore.UserFirestoreDataSourceImplementation
import com.example.wayspot.data.dto.UserFirestoreDto
import com.example.wayspot.data.model.user.UserRelationshipListType
import com.example.wayspot.data.repository.UserRepository
import com.google.common.truth.Truth.assertThat
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.tasks.await
import org.junit.After
import org.junit.Before
import org.junit.Test

class UserRepositoryIntegrationTest {
    private val db = Firebase.firestore
    private val firstUserId = "repository_test_user_1"
    private val secondUserId = "repository_test_user_2"
    private val thirdUserId = "repository_test_user_3"
    private val reviewId = "repository_test_review"
    private lateinit var dataSource: UserFirestoreDataSourceImplementation
    private lateinit var repository: UserRepository

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

        db.collection("users").document(firstUserId)
            .set(generateUser(firstUserId, "Usuario Uno", "usuario_uno")).await()
        db.collection("users").document(secondUserId)
            .set(generateUser(secondUserId, "Usuario Dos", "usuario_dos")).await()
        db.collection("users").document(thirdUserId)
            .set(generateUser(thirdUserId, "Usuario Tres", "usuario_tres")).await()

        dataSource = UserFirestoreDataSourceImplementation(db)
        repository = UserRepository(dataSource)
    }

    // Obtiene los perfiles desde Firestore y verifica que el repositorio devuelve sus usuarios de dominio.
    @Test
    fun getUsers_returnsMappedUsers() = runTest {
        val result = repository.getUsers()

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()?.map { it.id }).containsAtLeast(
            firstUserId,
            secondUserId,
            thirdUserId
        )
    }

    // Pide IDs repetidos y comprueba que devuelve cada perfil una sola vez.
    @Test
    fun getUsersByIds_returnsMatchingUsersWithoutDuplicates() = runTest {
        val result = repository.getUsersByIds(listOf(secondUserId, firstUserId, secondUserId))

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()?.map { it.id }).containsExactly(firstUserId, secondUserId)
    }

    // Busca un usuario y verifica que el repositorio conserva sus datos al convertirlos al modelo de la app.
    @Test
    fun getUserById_returnsMappedUser() = runTest {
        val result = repository.getUserById(firstUserId)
        val user = result.getOrNull()

        assertThat(result.isSuccess).isTrue()
        assertThat(user?.id).isEqualTo(firstUserId)
        assertThat(user?.name).isEqualTo("Usuario Uno")
        assertThat(user?.username).isEqualTo("usuario_uno")
        assertThat(user?.location).isEqualTo("Colombia")
    }

    // Busca un usuario inexistente y comprueba que el repositorio devuelve un fallo legible.
    @Test
    fun getUserById_whenUserDoesNotExist_returnsFailure() = runTest {
        val result = repository.getUserById("repository_test_missing_user")

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Usuario repository_test_missing_user no encontrado")
    }

    // Actualiza la foto y vuelve a consultar el perfil para comprobar que la URL quedó guardada.
    @Test
    fun updateUserAvatar_persistsAvatarUrl() = runTest {
        val result = repository.updateUserAvatar(firstUserId, "https://example.test/avatar.png")
        val updatedUser = repository.getUserById(firstUserId)

        assertThat(result.isSuccess).isTrue()
        assertThat(updatedUser.getOrNull()?.avatarUrl).isEqualTo("https://example.test/avatar.png")
    }

    // Consulta una relación que no existe y comprueba que el resultado sea false.
    @Test
    fun isFollowing_returnsFalseWhenRelationshipDoesNotExist() = runTest {
        val result = repository.isFollowing(firstUserId, secondUserId)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isFalse()
    }

    // Sigue a otro usuario y comprueba la relación, el contador de seguidos y el de seguidores.
    @Test
    fun followOrUnfollowUser_whenNotFollowing_createsRelationshipAndUpdatesCounts() = runTest {
        val result = repository.followOrUnfollowUser(firstUserId, secondUserId)
        val isFollowing = repository.isFollowing(firstUserId, secondUserId)
        val followingCount = repository.getFollowingCount(firstUserId)
        val followersCount = repository.getFollowersCount(secondUserId)

        assertThat(result.isSuccess).isTrue()
        assertThat(isFollowing.getOrNull()).isTrue()
        assertThat(followingCount.getOrNull()).isEqualTo(1)
        assertThat(followersCount.getOrNull()).isEqualTo(1)
    }

    // Parte de una relación existente, deja de seguir y comprueba que se eliminó y ambos contadores volvieron a cero.
    @Test
    fun followOrUnfollowUser_whenAlreadyFollowing_removesRelationshipAndResetsCounts() = runTest {
        dataSource.followOrUnfollowUser(firstUserId, secondUserId)

        val result = repository.followOrUnfollowUser(firstUserId, secondUserId)
        val isFollowing = repository.isFollowing(firstUserId, secondUserId)
        val followingCount = repository.getFollowingCount(firstUserId)
        val followersCount = repository.getFollowersCount(secondUserId)

        assertThat(result.isSuccess).isTrue()
        assertThat(isFollowing.getOrNull()).isFalse()
        assertThat(followingCount.getOrNull()).isEqualTo(0)
        assertThat(followersCount.getOrNull()).isEqualTo(0)
    }

    // Crea una relación de seguimiento y comprueba que devuelve el perfil seguido.
    @Test
    fun getUserConnections_returnsFollowingProfiles() = runTest {
        dataSource.followOrUnfollowUser(firstUserId, secondUserId)

        val result = repository.getUserConnections(firstUserId, UserRelationshipListType.FOLLOWING)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()?.map { it.id }).containsExactly(secondUserId)
        assertThat(result.getOrNull()?.first()?.name).isEqualTo("Usuario Dos")
    }

    // Crea una relación y comprueba que el repositorio devuelve el ID del usuario seguido.
    @Test
    fun getFollowingUserIds_returnsFollowingIds() = runTest {
        dataSource.followOrUnfollowUser(firstUserId, thirdUserId)

        val result = repository.getFollowingUserIds(firstUserId)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).contains(thirdUserId)
    }

    // Guarda una reseña y comprueba que el repositorio devuelve sus datos y el ID del documento de Firestore.
    @Test
    fun getReviewsByUserId_returnsMappedReviewWithDocumentId() = runTest {
        db.collection("reviews").document(reviewId).set(
            mapOf(
                "rating" to 5,
                "title" to "Buena experiencia",
                "description" to "Reseña de prueba",
                "userId" to firstUserId,
                "placeId" to "repository_test_place",
                "likesCount" to 0
            )
        ).await()

        val result = repository.getReviewsByUserId(firstUserId)
        val review = result.getOrNull()?.single()

        assertThat(result.isSuccess).isTrue()
        assertThat(review?.id).isEqualTo(reviewId)
        assertThat(review?.title).isEqualTo("Buena experiencia")
    }

    private fun generateUser(id: String, name: String, username: String) = UserFirestoreDto(
        id = id,
        name = name,
        username = username,
        fcmToken = "test-token",
        country = "Colombia",
        bio = "Bio de prueba"
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
