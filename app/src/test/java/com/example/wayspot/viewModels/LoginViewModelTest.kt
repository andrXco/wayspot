package com.example.wayspot.viewModels

import com.example.wayspot.data.repository.AuthRepository
import com.example.wayspot.data.repository.UserRepository
import com.example.wayspot.ui.screens.auth.login.LoginViewModel
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
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
class LoginViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var authRepository: AuthRepository
    private lateinit var userRepository: UserRepository
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        authRepository = mockk(relaxed = true)
        userRepository = mockk(relaxed = true)
        viewModel = LoginViewModel(authRepository, userRepository)
    }

    // Cambia el correo y la contraseña y verifica que ambos quedan en el estado del formulario.
    @Test
    fun updateUsuarioAndContrasena_updatesFormState() {
        viewModel.updateUsuario("usuario@example.com")
        viewModel.updateContrasena("123456")

        assertThat(viewModel.uiState.value.usuario).isEqualTo("usuario@example.com")
        assertThat(viewModel.uiState.value.contrasena).isEqualTo("123456")
    }

    // Activa la visibilidad y comprueba que la contraseña queda marcada como visible.
    @Test
    fun togglePasswordVisibility_changesVisibility() {
        viewModel.togglePasswordVisibility()

        assertThat(viewModel.uiState.value.passwordVisible).isTrue()
    }

    // Simula un inicio de sesión correcto y comprueba que el ViewModel solicita navegar al inicio.
    @Test
    fun signIn_whenRepositorySucceeds_emitsNavigationEvent() = runTest(testDispatcher) {
        coEvery { authRepository.signIn("usuario@example.com", "123456") } returns Result.success(Unit)
        every { authRepository.currentUser } returns null
        viewModel.updateUsuario(" usuario@example.com ")
        viewModel.updateContrasena("123456")

        viewModel.signIn()
        advanceUntilIdle()

        assertThat(viewModel.navigateToHome.value).isTrue()
        assertThat(viewModel.mostrarMensajeError.value).isFalse()
    }

    // Simula credenciales rechazadas y comprueba que aparece el mensaje de error.
    @Test
    fun signIn_whenRepositoryFails_emitsErrorEvent() = runTest(testDispatcher) {
        coEvery { authRepository.signIn(any(), any()) } returns
            Result.failure(IllegalArgumentException("Credenciales incorrectas"))
        viewModel.updateUsuario("usuario@example.com")
        viewModel.updateContrasena("incorrecta")

        viewModel.signIn()
        advanceUntilIdle()

        assertThat(viewModel.navigateToHome.value).isFalse()
        assertThat(viewModel.mostrarMensajeError.value).isTrue()
        assertThat(viewModel.mensajeError.value).isEqualTo("Credenciales incorrectas")
    }

    // Consume el evento de navegación y comprueba que vuelve a false.
    @Test
    fun onNavigationHandled_resetsNavigationEvent() = runTest(testDispatcher) {
        coEvery { authRepository.signIn(any(), any()) } returns Result.success(Unit)
        every { authRepository.currentUser } returns null
        viewModel.signIn()
        advanceUntilIdle()

        viewModel.onNavigationHandled()

        assertThat(viewModel.navigateToHome.value).isFalse()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
