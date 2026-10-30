package com.example.wayspot.repository

import com.example.wayspot.data.datasource.UserRemoteDataSource
import com.example.wayspot.data.dto.UserFirestoreDto
import com.example.wayspot.data.repository.UserRepository
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UserRepositoryTest {
    private lateinit var dataSource: UserRemoteDataSource
    private lateinit var repository: UserRepository

    @Before
    fun setUp() {
        dataSource = mockk()
        repository = UserRepository(dataSource)
    }

    // Simula un usuario existente y verifica que el repositorio transforma sus datos al modelo de la app.
    @Test
    fun getUserById_whenUserExists_returnsMappedUser() = runTest {
        coEvery { dataSource.getUserById("user_1") } returns userDto("user_1")

        val result = repository.getUserById("user_1")

        Truth.assertThat(result.isSuccess).isTrue()
        Truth.assertThat(result.getOrNull()?.id).isEqualTo("user_1")
        Truth.assertThat(result.getOrNull()?.name).isEqualTo("Usuario Uno")
        Truth.assertThat(result.getOrNull()?.location).isEqualTo("Colombia")
    }

    // Simula un error del data source y comprueba que el repositorio conserva esa misma causa.
    @Test
    fun getUserById_whenDataSourceFails_returnsSameFailure() = runTest {
        val exception = NoSuchElementException("Usuario no encontrado")
        coEvery { dataSource.getUserById("missing") } throws exception

        val result = repository.getUserById("missing")

        Truth.assertThat(result.isFailure).isTrue()
        Truth.assertThat(result.exceptionOrNull()).isSameInstanceAs(exception)
    }

    // Simula una búsqueda sin resultado y verifica que el repositorio la convierte en un fallo.
    @Test
    fun getUserById_whenUserDoesNotExist_returnsFailure() = runTest {
        coEvery { dataSource.getUserById("user_999") } returns null

        val result = repository.getUserById("user_999")

        Truth.assertThat(result.isFailure).isTrue()
        Truth.assertThat(result.exceptionOrNull()?.message)
            .isEqualTo("Usuario user_999 no encontrado")
    }

    // Simula varios usuarios y comprueba que el repositorio devuelve sus perfiles mapeados.
    @Test
    fun getUsers_mapsAllUsers() = runTest {
        coEvery { dataSource.getUsers() } returns listOf(
            userDto("user_1"),
            userDto("user_2", name = "Usuario Dos")
        )

        val result = repository.getUsers()

        Truth.assertThat(result.isSuccess).isTrue()
        Truth.assertThat(result.getOrNull()?.map { it.id }).containsExactly("user_1", "user_2")
    }

    // Pide IDs repetidos y comprueba que el repositorio consulta cada ID una sola vez.
    @Test
    fun getUsersByIds_removesDuplicateIdsBeforeQueryingDataSource() = runTest {
        coEvery { dataSource.getUsersByIds(listOf("user_1", "user_2")) } returns listOf(
            userDto("user_1"),
            userDto("user_2", name = "Usuario Dos")
        )

        val result = repository.getUsersByIds(listOf("user_1", "user_2", "user_1"))

        Truth.assertThat(result.isSuccess).isTrue()
        Truth.assertThat(result.getOrNull()?.map { it.id }).containsExactly("user_1", "user_2")
        coVerify(exactly = 1) { dataSource.getUsersByIds(listOf("user_1", "user_2")) }
    }

    // Simula un perfil sin biografía ni país y comprueba que esos campos sigan siendo null.
    @Test
    fun getUserById_preservesNullableProfileFields() = runTest {
        coEvery { dataSource.getUserById("user_1") } returns userDto(
            id = "user_1",
            bio = null,
            country = null
        )

        val result = repository.getUserById("user_1")

        Truth.assertThat(result.isSuccess).isTrue()
        Truth.assertThat(result.getOrNull()?.bio).isNull()
        Truth.assertThat(result.getOrNull()?.location).isNull()
    }

    // Simula que un usuario sigue a otro y comprueba que el resultado se conserva.
    @Test
    fun isFollowing_returnsDataSourceValue() = runTest {
        coEvery { dataSource.isFollowing("user_1", "user_2") } returns true

        val result = repository.isFollowing("user_1", "user_2")

        Truth.assertThat(result.isSuccess).isTrue()
        Truth.assertThat(result.getOrNull()).isTrue()
    }

    // Simula una actualización correcta y verifica que el repositorio envía el ID y la URL al data source.
    @Test
    fun updateUserAvatar_returnsSuccessAndDelegatesArguments() = runTest {
        coEvery {
            dataSource.updateUserAvatar("user_1", "https://example.test/avatar.png")
        } returns Unit

        val result = repository.updateUserAvatar("user_1", "https://example.test/avatar.png")

        Truth.assertThat(result.isSuccess).isTrue()
        coVerify(exactly = 1) {
            dataSource.updateUserAvatar("user_1", "https://example.test/avatar.png")
        }
    }

    // Simula IDs de seguimiento repetidos y comprueba que el repositorio los devuelve sin duplicados.
    @Test
    fun getFollowingUserIds_returnsUniqueIdsAsSet() = runTest {
        coEvery {
            dataSource.getUserConnectionIds("user_1", any())
        } returns listOf("user_2", "user_3", "user_2")

        val result = repository.getFollowingUserIds("user_1")

        Truth.assertThat(result.isSuccess).isTrue()
        Truth.assertThat(result.getOrNull()).containsExactly("user_2", "user_3")
    }

    private fun userDto(
        id: String,
        name: String = "Usuario Uno",
        bio: String? = "Biografia",
        country: String? = "Colombia"
    ) = UserFirestoreDto(
        id = id,
        name = name,
        username = id,
        fcmToken = "test-token",
        country = country,
        bio = bio
    )
}