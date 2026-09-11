package com.example.wayspot.data.datasource.impl.firestore

import com.example.wayspot.data.datasource.UserRemoteDataSource
import com.example.wayspot.data.datasource.UsernameAlreadyExistsException
import com.example.wayspot.data.dtos.RegisterUserDto
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

class UserFirestoreDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : UserRemoteDataSource {
    override suspend fun registerUser(
        registerUserDto: RegisterUserDto,
        userId: String
    ) {
        val profileReference = firestore.collection(USERS_COLLECTION).document(userId)
        val usernameReference = firestore.collection(USERNAMES_COLLECTION)
            .document(registerUserDto.usernameNormalized)
        firestore.runTransaction { transaction ->
            if (transaction.get(usernameReference).exists()) {
                throw UsernameAlreadyExistsException()
            }
            transaction.set(usernameReference, mapOf(USER_ID_FIELD to userId))
            transaction.set(
                profileReference,
                mapOf(
                    USER_ID_FIELD to userId,
                    EMAIL_FIELD to registerUserDto.email,
                    USERNAME_FIELD to registerUserDto.username,
                    USERNAME_NORMALIZED_FIELD to registerUserDto.usernameNormalized,
                    CREATED_AT_FIELD to FieldValue.serverTimestamp()
                )
            )
        }.await()
    }

    override suspend fun deleteUser(userId: String) {
        val profileReference = firestore.collection(USERS_COLLECTION).document(userId)
        firestore.runTransaction { transaction ->
            val profile = transaction.get(profileReference)
            val normalizedUsername = profile.getString(USERNAME_NORMALIZED_FIELD)
            if (normalizedUsername != null) {
                transaction.delete(
                    firestore.collection(USERNAMES_COLLECTION).document(normalizedUsername)
                )
            }
            transaction.delete(profileReference)
        }.await()
    }

    private companion object {
        const val USERS_COLLECTION = "users"
        const val USERNAMES_COLLECTION = "usernames"
        const val USER_ID_FIELD = "uid"
        const val EMAIL_FIELD = "email"
        const val USERNAME_FIELD = "username"
        const val USERNAME_NORMALIZED_FIELD = "usernameNormalized"
        const val CREATED_AT_FIELD = "createdAt"
    }
}
