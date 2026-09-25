package com.example.wayspot.data.repository

import android.net.Uri
import com.example.wayspot.data.datasource.AuthRemoteDataSource
import com.example.wayspot.data.datasource.StorageRemoteDataSource
import javax.inject.Inject

class StorageRepository @Inject constructor(
    private val storage: StorageRemoteDataSource,
    private val auth: AuthRemoteDataSource
) {

    suspend fun uploadProfileImage(uri: Uri): Result<String> {
        val userId = auth.currentUser?.uid
            ?: return Result.failure(
                Exception("Debes iniciar sesión para cambiar tu foto de perfil")
            )

        return try {
            val path = "profileImages/$userId.jpg"

            val url = storage.uploadImage(path, uri)

            auth.updateProfileImage(url)

            Result.success(url)
        } catch (e: Exception) {
            Result.failure(
                Exception("No se pudo subir la foto de perfil")
            )
        }
    }
}