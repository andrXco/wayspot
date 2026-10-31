package com.example.wayspot.viewModels

import com.example.wayspot.data.repository.AuthRepository
import com.example.wayspot.data.repository.UserRepository
import com.example.wayspot.ui.screens.auth.signup.SignUpViewModel
import com.google.common.truth.Truth.assertThat
import com.google.firebase.auth.FirebaseUser
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RegisterViewModelUnitTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var authRepository: AuthRepository
    private lateinit var userRepository: UserRepository
    private lateinit var viewModel: SignUpViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        authRepository = mockk(relaxed = true)
        userRepository = mockk(relaxed = true)
        viewModel = SignUpViewModel(authRepository, userRepository)
    }

    // Deja los nombres vacíos y comprueba que se muestra el error sin llamar a Auth.
    @Test
    fun signUp_whenRequiredNamesAreMissing_showsErrorWithoutCallingAuth() = runTest(testDispatcher) {
        viewModel.signUp()
        advanceUntilIdle()

        assertThat(viewModel.mostrarMensajeError.value).isTrue()
        assertThat(viewModel.mensajeError.value).isEqualTo("Completa el nombre y el nombre de usuario")
        coVerify(exactly = 0) { authRepository.signUp(any(), any()) }
    }

    // Simula un fallo al crear la cuenta y comprueba que el error se muestra sin guardar el perfil.
    @Test
    fun signUp_whenAuthFails_showsAuthError() = runTest(testDispatcher) {
        coEvery { authRepository.signUp(any(), any()) } returns
            Result.failure(IllegalArgumentException("El correo no es válido"))
        fillRequiredFields()

        viewModel.signUp()
        advanceUntilIdle()

        assertThat(viewModel.navigateToHome.value).isFalse()
        assertThat(viewModel.mostrarMensajeError.value).isTrue()
        assertThat(viewModel.mensajeError.value).isEqualTo("El correo no es válido")
        coVerify(exactly = 0) { userRepository.registerUser(any(), any(), any(), any(), any()) }
    }

    // Simula Auth y el guardado del perfil correctos y verifica el evento de navegación.
    @Test
    fun signUp_whenAuthAndProfileSaveSucceed_emitsNavigationEvent() = runTest(testDispatcher) {
        coEvery { authRepository.signUp("test@test.com", "123456") } returns Result.success(Unit)
        every { authRepository.currentUser } returns mockk<FirebaseUser> {
            every { uid } returns "test-auth-user"
        }
        coEvery { userRepository.registerUser(any(), any(), any(), any(), any()) } returns Result.success(Unit)
        fillRequiredFields()

        viewModel.signUp()
        advanceUntilIdle()

        assertThat(viewModel.navigateToHome.value).isTrue()
        assertThat(viewModel.mostrarMensajeError.value).isFalse()
        coVerify(exactly = 1) {
            userRepository.registerUser(
                username = "usuario",
                name = "Usuario de prueba",
                country = null,
                bio = null,
                userId = "test-auth-user"
            )
        }
    }

    // Simula que Auth funciona pero falla el guardado del perfil y verifica el error.
    @Test
    fun signUp_whenProfileSaveFails_showsProfileError() = runTest(testDispatcher) {
        coEvery { authRepository.signUp(any(), any()) } returns Result.success(Unit)
        every { authRepository.currentUser } returns mockk<FirebaseUser> {
            every { uid } returns "test-auth-user"
        }
        coEvery { userRepository.registerUser(any(), any(), any(), any(), any()) } returns
            Result.failure(IllegalStateException("No se pudo guardar el perfil"))
        fillRequiredFields()

        viewModel.signUp()
        advanceUntilIdle()

        assertThat(viewModel.navigateToHome.value).isFalse()
        assertThat(viewModel.mostrarMensajeError.value).isTrue()
        assertThat(viewModel.mensajeError.value).isEqualTo("No se pudo guardar el perfil")
    }

    // Consume el evento de navegación al inicio y comprueba que queda restablecido.
    @Test
    fun onNavigationHandled_resetsNavigationEvent() = runTest(testDispatcher) {
        coEvery { authRepository.signUp(any(), any()) } returns Result.success(Unit)
        every { authRepository.currentUser } returns mockk<FirebaseUser> {
            every { uid } returns "test-auth-user"
        }
        coEvery { userRepository.registerUser(any(), any(), any(), any(), any()) } returns Result.success(Unit)
        fillRequiredFields()

        viewModel.signUp()
        advanceUntilIdle()
        viewModel.onNavigationHandled()

        assertThat(viewModel.navigateToHome.value).isFalse()
    }

    private fun fillRequiredFields() {
        viewModel.updateNombre("usuario")
        viewModel.updateNombreCompleto("Usuario de prueba")
        viewModel.updateCorreo("test@test.com")
        viewModel.updateContrasena("123456")
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
