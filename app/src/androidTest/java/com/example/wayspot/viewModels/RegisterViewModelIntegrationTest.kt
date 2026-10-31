package com.example.wayspot.viewModels

import android.os.Build
import androidx.test.platform.app.InstrumentationRegistry
import com.example.wayspot.data.datasource.AuthRemoteDataSource
import com.example.wayspot.data.datasource.implementation.firestore.UserFirestoreDataSourceImplementation
import com.example.wayspot.data.repository.AuthRepository
import com.example.wayspot.data.repository.UserRepository
import com.example.wayspot.ui.screens.auth.signup.SignUpViewModel
import com.google.common.truth.Truth.assertThat
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.tasks.await
import org.junit.After
import org.junit.Before
import org.junit.Test

class RegisterViewModelIntegrationTest {
    private lateinit var viewModel: SignUpViewModel

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
        val authRepository = AuthRepository(AuthRemoteDataSource(Firebase.auth))
        val userRepository = UserRepository(UserFirestoreDataSourceImplementation(Firebase.firestore))
        viewModel = SignUpViewModel(authRepository, userRepository)
    }

    // Registra la cuenta en Auth y comprueba que ViewModel y repositorios guardan el perfil en Firestore.
    @Test
    fun signUp_validInputs_createsAuthUserAndFirestoreProfile() = runTest {
        viewModel.updateNombre("usuario_prueba")
        viewModel.updateNombreCompleto("Usuario de prueba")
        viewModel.updateCorreo("viewmodel@prueba.com")
        viewModel.updateContrasena("TestPassword123!")

        viewModel.signUp()
        viewModel.navigateToHome.first { it }

        val user = Firebase.auth.currentUser
        assertThat(user).isNotNull()
        val profile = Firebase.firestore.collection("users").document(user!!.uid).get().await()
        assertThat(profile.getString("username")).isEqualTo("usuario_prueba")
        assertThat(profile.getString("name")).isEqualTo("Usuario de prueba")
    }

    // Intenta registrar una contraseña demasiado corta y comprueba que Auth muestra el error sin crear una sesión.
    @Test
    fun signUp_shortPassword_showsError() = runTest {
        viewModel.updateNombre("usuario_prueba")
        viewModel.updateNombreCompleto("Usuario de prueba")
        viewModel.updateCorreo("viewmodel@prueba.com")
        viewModel.updateContrasena("12345")

        viewModel.signUp()
        viewModel.mostrarMensajeError.first { it }

        assertThat(viewModel.mensajeError.value).isEqualTo("La contraseña debe tener al menos 6 caracteres")
        assertThat(viewModel.navigateToHome.value).isFalse()
        assertThat(Firebase.auth.currentUser).isNull()
    }

    // Intenta registrarse sin nombres y comprueba que aparece el error sin crear una cuenta.
    @Test
    fun signUp_emptyNames_showsErrorWithoutCreatingUser() = runTest {
        viewModel.signUp()
        viewModel.mostrarMensajeError.first { it }

        assertThat(viewModel.mensajeError.value).isEqualTo("Completa el nombre y el nombre de usuario")
        assertThat(Firebase.auth.currentUser).isNull()
    }

    @After
    fun tearDown() = runTest {
        val user = Firebase.auth.currentUser
        if (user != null) {
            Firebase.firestore.collection("users").document(user.uid).delete().await()
            user.delete().await()
        }
        Firebase.auth.signOut()
    }
}
