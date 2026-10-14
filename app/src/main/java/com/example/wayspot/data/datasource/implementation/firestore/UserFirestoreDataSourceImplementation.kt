
package com.example.wayspot.data.datasource.implementation.firestore

import com.example.wayspot.data.datasource.UserRemoteDataSource
import com.example.wayspot.data.dto.RegisterUserDto
import com.example.wayspot.data.dto.ReviewDto
import com.example.wayspot.data.dto.UserDto
import com.example.wayspot.data.dto.UserFirestoreDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserFirestoreDataSourceImplementation @Inject constructor(
    private val db: FirebaseFirestore
) : UserRemoteDataSource {

    override suspend fun getUsers(): List<UserDto> {
        TODO("Not yet implemented")
    }

    override suspend fun getUserById(id: String): UserFirestoreDto {

        val docRef = db.collection("users").document(id)

        val snapshot = docRef.get().await()

        val user = snapshot.toObject(UserFirestoreDto::class.java)
            ?: throw Exception("No se pudieron obtener los datos del usuario")

        return user.copy(id = snapshot.id)
    }

    override suspend fun getReviewsByUserId(
        userId: String
    ): List<ReviewDto> {
        TODO("Not yet implemented")
    }

    override suspend fun registerUser(
        registerUserDto: RegisterUserDto,
        userId: String
    ) {
        val docRef = db.collection("users").document(userId)
        docRef.set(registerUserDto).await()
    }

    suspend fun updateUser(
        userDto: RegisterUserDto,
        userId: String
    ) {
        val docRef = db.collection("users").document(userId)
        docRef.set(userDto).await()
    }

}
