
package com.example.wayspot.data.datasource.implementation.firestore

import com.example.wayspot.data.datasource.UserRemoteDataSource
import com.example.wayspot.data.dto.RegisterUserDto
import com.example.wayspot.data.dto.ReviewFirestoreDto
import com.example.wayspot.data.dto.UserConnectionsPageDto
import com.example.wayspot.data.dto.UserFirestoreDto
import com.example.wayspot.data.model.user.UserRelationshipListType
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
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
                "avatarUrl" to userDto.avatarUrl,
                "fcmToken" to userDto.fcmToken,
                "notificationPreferences" to mapOf(
                    "newFollowers" to userDto.notificationPreferences.newFollowers,
                    "reviewComments" to userDto.notificationPreferences.reviewComments,
                    "likesReceived" to userDto.notificationPreferences.likesReceived
                )
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
            fcmToken = user.fcmToken,
            country = user.country,
            bio = user.bio,
            avatarUrl = avatarUrl,
            notificationPreferences = user.notificationPreferences
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

    override suspend fun getUserConnections(
        userId: String,
        relationshipType: UserRelationshipListType,
        cursor: String?,
        pageSize: Long
    ): UserConnectionsPageDto {
        val userReference = db.collection("users").document(userId)
        val connectionsCollection = when (relationshipType) {
            UserRelationshipListType.FOLLOWERS -> userReference.collection("followers")
            UserRelationshipListType.FOLLOWING -> userReference.collection("following")
        }

        val connectionsQuery = connectionsCollection
            .orderBy("timestamp")
            .orderBy(FieldPath.documentId())

        val cursorValues = cursor?.split(CURSOR_SEPARATOR, limit = 3)
        val cursorTimestampSeconds = cursorValues?.getOrNull(0)?.toLongOrNull()
        val cursorTimestampNanoseconds = cursorValues?.getOrNull(1)?.toIntOrNull()
        val cursorDocumentId = cursorValues?.getOrNull(2)
        val pagedConnectionsQuery = if (
            cursorTimestampSeconds != null && cursorTimestampNanoseconds != null &&
            !cursorDocumentId.isNullOrBlank()
        ) {
            connectionsQuery.startAfter(
                Timestamp(cursorTimestampSeconds, cursorTimestampNanoseconds),
                cursorDocumentId
            )
        } else {
            connectionsQuery
        }
        val connectionsSnapshot = pagedConnectionsQuery
            .limit(pageSize)
            .get()
            .await()

        val connectedUserIds = connectionsSnapshot.documents.map { it.id }
        val usersById = if (connectedUserIds.isEmpty()) {
            emptyMap()
        } else {
            db.collection("users")
                .whereIn(FieldPath.documentId(), connectedUserIds)
                .get()
                .await()
                .documents
                .mapNotNull { document ->
                    document.toObject(UserFirestoreDto::class.java)
                        ?.copy(id = document.id)
                        ?.let { document.id to it }
                }
                .toMap()
        }

        val users = connectedUserIds.mapNotNull(usersById::get)
        val lastConnectionDocument = connectionsSnapshot.documents.lastOrNull()
        val lastTimestamp = lastConnectionDocument?.getTimestamp("timestamp")
        val nextCursor = if (
            lastConnectionDocument != null && lastTimestamp != null &&
            connectionsSnapshot.documents.size.toLong() == pageSize
        ) {
            listOf(
                lastTimestamp.seconds.toString(),
                lastTimestamp.nanoseconds.toString(),
                lastConnectionDocument.id
            ).joinToString(CURSOR_SEPARATOR)
        } else {
            null
        }

        return UserConnectionsPageDto(
            users = users,
            nextCursor = nextCursor
        )
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

    private companion object {
        const val CURSOR_SEPARATOR = ":"
    }
}
