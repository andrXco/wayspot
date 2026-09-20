package com.example.wayspot.data.repository
import android.net.Uri
import com.example.wayspot.data.datasource.AuthRemoteDataSource
import com.example.wayspot.data.datasource.StorageRemoteDataSource
import java.util.UUID
import javax.inject.Inject

class StorageRepository @Inject constructor(
    private val storage: StorageRemoteDataSource,
    private val auth: AuthRemoteDataSource
) {

    suspend fun uploadProfileImage(uri: Uri): Result<String> {
        val userId = auth.currentUser?.uid
            ?: return Result.failure(IllegalStateException("Authenticated user is required."))

        return try {
            val path = "profileImages/$userId-${UUID.randomUUID()}.jpg"
            val url = storage.uploadImage(path, uri)
            auth.updateProfileImage(url)
            Result.success(url)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun uploadBackgroundImage(uri: Uri): Result<String> {
        val userId = auth.currentUser?.uid
            ?: return Result.failure(IllegalStateException("Authenticated user is required."))

        return try {
            val path = "backgroundImages/$userId.jpg"
            val url = storage.uploadImage(path, uri)
            Result.success(url)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
