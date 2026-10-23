
package com.example.wayspot.data.datasource.implementation.firestore

import com.example.wayspot.data.datasource.UserRemoteDataSource
import com.example.wayspot.data.dto.RegisterUserDto
import com.example.wayspot.data.dto.UserFirestoreDto
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.example.wayspot.data.dto.ReviewFirestoreDto
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserFirestoreDataSourceImplementation @Inject constructor(
    private val db: FirebaseFirestore
) : UserRemoteDataSource {


    override suspend fun getUsers(): List<UserFirestoreDto> {

        val snapshot = db.collection("users")
            .get()
            .await()

        val users = mutableListOf<UserFirestoreDto>()

        for (document in snapshot.documents) {
            val user = document.toObject(
                UserFirestoreDto::class.java
            )

            if (user != null) {
                users.add(user.copy(id = document.id))
            }
        }

        return users
    }

    override suspend fun getUserById(id: String): UserFirestoreDto {

        val docRef = db.collection("users").document(id)

        val snapshot = docRef.get().await()

        val user = snapshot.toObject(UserFirestoreDto::class.java)
            ?: throw Exception("No se pudieron obtener los datos del usuario")

        return user.copy(id = snapshot.id)
    }

    override suspend fun registerUser(
        registerUserDto: RegisterUserDto,
        userId: String
    ) {
        val docRef = db.collection("users").document(userId)
        docRef.set(registerUserDto).await()
    }

    override suspend fun updateUser(
        userDto: RegisterUserDto,
        userId: String
    ) {
        val docRef = db.collection("users").document(userId)

        docRef.update(
            mapOf(
                "username" to userDto.username,
                "name" to userDto.name,
                "country" to userDto.country,
                "bio" to userDto.bio,
                "avatarUrl" to userDto.avatarUrl
            )
        ).await()
    }

    override suspend fun updateUserAvatar(
        userId: String,
        avatarUrl: String
    ) {
        val user = getUserById(userId)

        val userDto = RegisterUserDto(
            username = user.username,
            name = user.name,
            country = user.country,
            bio = user.bio,
            avatarUrl = avatarUrl
        )

        updateUser(userDto, userId)
    }


    override suspend fun isFollowing(
        followerId: String,
        followedId: String
    ): Boolean {
        return db.collection("users")
            .document(followerId)
            .collection("following")
            .document(followedId)
            .get()
            .await()
            .exists()
    }


    override suspend fun getFollowersCount(userId: String): Int {
        val snapshot = db.collection("users")
            .document(userId)
            .get()
            .await()

        return snapshot.getLong("followersCount")?.toInt() ?: 0
    }



    override suspend fun getFollowingCount(userId: String): Int {
        val snapshot = db.collection("users")
            .document(userId)
            .get()
            .await()

        return snapshot.getLong("followingCount")?.toInt() ?: 0
    }

    override suspend fun followOrUnfollowUser(
        currentUserId: String,
        targetUserId: String
    ) {
        val currentUserRef = db.collection("users")
            .document(currentUserId)

        val targetUserRef = db.collection("users")
            .document(targetUserId)

        val followingRef = currentUserRef.collection("following")
            .document(targetUserId)

        val followersRef = targetUserRef.collection("followers")
            .document(currentUserId)

        db.runTransaction { transaction ->

            val followingDoc = transaction.get(followingRef)

            if (followingDoc.exists()) {

                transaction.delete(followingRef)

                transaction.delete(followersRef)

                transaction.update(
                    currentUserRef,
                    "followingCount",
                    FieldValue.increment(-1L)
                )

                transaction.update(
                    targetUserRef,
                    "followersCount",
                    FieldValue.increment(-1L)
                )

            } else {

                transaction.set(
                    followingRef,
                    mapOf(
                        "timestamp" to FieldValue.serverTimestamp()
                    )
                )

                transaction.set(
                    followersRef,
                    mapOf(
                        "timestamp" to FieldValue.serverTimestamp()
                    )
                )

                transaction.update(
                    currentUserRef,
                    "followingCount",
                    FieldValue.increment(1L)
                )

                transaction.update(
                    targetUserRef,
                    "followersCount",
                    FieldValue.increment(1L)
                )
            }
        }.await()
    }

    override suspend fun getReviewsByUserId(
        userId: String
    ): List<ReviewFirestoreDto> {

        val snapshot = db.collection("reviews")
            .whereEqualTo("userId", userId)
            .get()
            .await()

        return snapshot.documents.map { document ->

            val review = document.toObject(
                ReviewFirestoreDto::class.java
            ) ?: throw Exception("No se pudo obtener la reseña")

            review.copy(id = document.id)
        }
    }

}
