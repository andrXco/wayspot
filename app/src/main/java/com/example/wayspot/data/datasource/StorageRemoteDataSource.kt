package com.example.wayspot.data.datasource

import android.content.Context
import android.net.Uri
import com.example.wayspot.BuildConfig
import com.example.wayspot.FIREBASE_EMULATOR_HOST
import com.example.wayspot.STORAGE_EMULATOR_PORT
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

//Datasource: Firebase Storage
class StorageRemoteDataSource @Inject constructor(
    private val storage: FirebaseStorage,
    @param:ApplicationContext private val context: Context
) {

    //Permite subir una imagen a Firebase Storage
    //Path: ubicacion/carpeta
    //Uri: ruta de la imagen en local
    suspend fun uploadImage(path: String, uri: Uri): String {
        val imageRef = storage.reference.child(path)
        val contentType = context.contentResolver.getType(uri)
            ?.takeIf { it.startsWith("image/") }
            ?: "image/jpeg"
        val metadata = StorageMetadata.Builder()
            .setContentType(contentType)
            .build()
        imageRef.putFile(uri, metadata).await()
        return imageRef.downloadUrl.await().toString()
    }

    suspend fun deleteImageByUrl(downloadUrl: String) {
        val uri = Uri.parse(downloadUrl)
        val reference = if (
            BuildConfig.DEBUG &&
            uri.scheme == "http" &&
            uri.host == FIREBASE_EMULATOR_HOST &&
            uri.port == STORAGE_EMULATOR_PORT
        ) {
            val segments = uri.pathSegments
            require(
                segments.size == 5 &&
                    segments[0] == "v0" &&
                    segments[1] == "b" &&
                    segments[2] == storage.reference.bucket &&
                    segments[3] == "o"
            ) { "La URL local no corresponde al bucket de Storage" }
            storage.reference.child(segments[4])
        } else {
            storage.getReferenceFromUrl(downloadUrl)
        }
        reference.delete().await()
    }

}
