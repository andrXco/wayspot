package com.example.wayspot.data.datasource

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import javax.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class AuthRemoteDataSource @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    fun observeUser(): Flow<FirebaseUser?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser)
        }
        firebaseAuth.addAuthStateListener(listener)
        awaitClose { firebaseAuth.removeAuthStateListener(listener) }
    }

    suspend fun signIn(email: String, password: String): FirebaseUser =
        firebaseAuth.signInWithEmailAndPassword(email, password).await().user
            ?: error("Firebase no devolvio un usuario autenticado.")

    suspend fun signUp(email: String, password: String): FirebaseUser =
        firebaseAuth.createUserWithEmailAndPassword(email, password).await().user
            ?: error("Firebase no devolvio un usuario registrado.")

    suspend fun updateDisplayName(username: String): FirebaseUser {
        val user = firebaseAuth.currentUser ?: error("No hay una sesion activa.")
        val request = UserProfileChangeRequest.Builder()
            .setDisplayName(username)
            .build()
        user.updateProfile(request).await()
        return user
    }

    suspend fun sendPasswordReset(email: String) {
        firebaseAuth.sendPasswordResetEmail(email).await()
    }

    suspend fun reauthenticate(password: String) {
        val user = firebaseAuth.currentUser ?: error("No hay una sesion activa.")
        val email = user.email ?: error("La sesion activa no tiene correo.")
        user.reauthenticate(EmailAuthProvider.getCredential(email, password)).await()
    }

    suspend fun deleteCurrentUser() {
        val user = firebaseAuth.currentUser ?: error("No hay una sesion activa.")
        user.delete().await()
    }

    fun signOut() {
        firebaseAuth.signOut()
    }
}
