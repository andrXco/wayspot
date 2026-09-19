package com.example.wayspot.data.repository

import com.example.wayspot.data.datasource.AuthRemoteDataSource
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

/**
 * Contrato de autenticación para la capa de presentación.
 *
 * Delega en la fuente remota y mantiene a los ViewModels ajenos a FirebaseAuth.
 */
class AuthRepository @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource
) {
    val currentUser: FirebaseUser?
        get() = authRemoteDataSource.currentUser

    suspend fun signIn(email: String, password: String): Result<Unit> {
        return try {
            authRemoteDataSource.signIn(email, password)
            Result.success(Unit)
        }
        catch (e: FirebaseAuthInvalidCredentialsException){
            Result.failure(Exception("Credenciales incorrectas"))
        } catch (e: FirebaseAuthInvalidUserException) {
            Result.failure(Exception("El usuario no existe"))
        }
        catch (e: Exception) {
            Result.failure(Exception("Error al iniciar sesion"))
        }

    }


    suspend fun signUp(email: String, password: String): Result<Unit> {
        try {
            authRemoteDataSource.signUp(email, password)
            return Result.success(Unit)
        }
        catch (e: Exception) {
            return Result.failure(e)
        }
    }



    fun signOut() {
        authRemoteDataSource.signOut()
    }
}
