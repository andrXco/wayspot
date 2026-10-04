package com.example.wayspot.data.datasource

import androidx.core.net.toUri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Encapsula el acceso directo a Firebase Authentication.
 *
 * Convierte las operaciones basadas en `Task` en llamadas suspendidas para que las capas
 * superiores no dependan de la API asíncrona del SDK.
 */
class AuthRemoteDataSource @Inject constructor(
    private val auth: FirebaseAuth
) {

    val currentUser: FirebaseUser?
        get() = auth.currentUser

    // Permite realizar Sign in con usuario y contraseña
    suspend fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    // Permite realizar Sign up con usuario y contraseña
    suspend fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).await()
    }


    //Permite cerrar sesion
    //No es necesario el suspend
    fun signOut() {
        auth.signOut()
    }

    //Permite actualizar la foto de perfil del usuario en FireAuth
    //Recibe la url de la imagen
    //Se debe llamar despues de subir la funcion a firebase
    suspend fun updateProfileImage(photoUrl: String) {
        val uri = photoUrl.toUri()
        val user = auth.currentUser ?: throw IllegalStateException("Authenticated user is required.")
        user.updateProfile(
            UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build()
        ).await()
    }

}
