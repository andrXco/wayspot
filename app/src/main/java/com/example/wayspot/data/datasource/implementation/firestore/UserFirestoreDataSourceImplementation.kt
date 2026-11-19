
package com.example.wayspot.data.datasource.implementation.firestore

import com.example.wayspot.data.datasource.UserRemoteDataSource
import com.example.wayspot.data.dto.RegisterUserDto
import com.example.wayspot.data.dto.ReviewFirestoreDto
import com.example.wayspot.data.dto.UserDtoGeneric
import com.example.wayspot.data.dto.UserFirestoreDto
import com.example.wayspot.data.dto.NotificationActorEventDto
import com.example.wayspot.data.dto.UserPrivateSettingsFirestoreDto
import com.example.wayspot.data.model.user.UserRelationshipListType
import com.example.wayspot.data.model.user.UserRelationshipRules
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserFirestoreDataSourceImplementation @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) : UserRemoteDataSource {

    private fun publicProfileData(user: RegisterUserDto): Map<String, Any?> = mapOf(
        "username" to user.username,
        "name" to user.name,
        "country" to user.country,
        "bio" to user.bio,
        "avatarUrl" to user.avatarUrl
    )

    private fun notificationPreferencesData(user: RegisterUserDto): Map<String, Boolean> = mapOf(
        "newFollowers" to user.notificationPreferences.newFollowers,
        "reviewComments" to user.notificationPreferences.reviewComments,
        "likesReceived" to user.notificationPreferences.likesReceived
    )

    private fun privateSettingsData(user: RegisterUserDto): Map<String, Any> = mapOf(
        "fcmToken" to user.fcmToken,
        "notificationPreferences" to notificationPreferencesData(user)
    )

    private suspend fun addCurrentUserPrivateSettings(
        userId: String,
        user: UserFirestoreDto
    ): UserFirestoreDto {
        if (auth.currentUser?.uid != userId) return user

        val settings = db.collection("users")
            .document(userId)
            .collection("private")
            .document("settings")
            .get()
            .await()
            .toObject(UserPrivateSettingsFirestoreDto::class.java)

        return user.copy(
            notificationPreferences = settings?.notificationPreferences
                ?: user.notificationPreferences
        )
    }

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

    override suspend fun getUsersByIds(userIds: List<String>): List<UserFirestoreDto> {
        if (userIds.isEmpty()) return emptyList()
        return db.collection("users")
            .whereIn(FieldPath.documentId(), userIds)
            .get()
            .await()
            .documents
            .mapNotNull { document ->
                document.toObject(UserFirestoreDto::class.java)?.copy(id = document.id)
            }
    }

    override suspend fun getUserById(id: String): UserFirestoreDto? {

        val docRef = db.collection("users").document(id)

        val snapshot = docRef.get().await()

        val user = snapshot.toObject(UserFirestoreDto::class.java)
            ?: return null

        return addCurrentUserPrivateSettings(
            userId = id,
            user = user.copy(id = snapshot.id)
        )
    }

    override suspend fun registerUser(
        registerUserDto: RegisterUserDto,
        userId: String
    ) {
        val userRef = db.collection("users").document(userId)
        val privateSettingsRef = userRef.collection("private").document("settings")
        db.batch()
            .set(userRef, publicProfileData(registerUserDto))
            .set(privateSettingsRef, privateSettingsData(registerUserDto))
            .commit()
            .await()
    }

    override suspend fun updateUser(
        userDto: RegisterUserDto,
        userId: String
    ) {
        val userRef = db.collection("users").document(userId)
        val privateSettingsRef = userRef.collection("private").document("settings")
        val updates = publicProfileData(userDto).toMutableMap()
        listOf(
            "fcmToken",
            "notificationPreferences",
            "followersCount",
            "followingCount",
            "email",
            "id"
        ).forEach { legacyField ->
            updates[legacyField] = FieldValue.delete()
        }

        val privateUpdates = mutableMapOf<String, Any>(
            "notificationPreferences" to notificationPreferencesData(userDto)
        )
        if (userDto.fcmToken.isNotBlank()) {
            privateUpdates["fcmToken"] = userDto.fcmToken
        }

        db.batch()
            .update(userRef, updates)
            .set(privateSettingsRef, privateUpdates, SetOptions.merge())
            .commit()
            .await()
    }

    override suspend fun updateUserAvatar(
        userId: String,
        avatarUrl: String
    ) {
        db.collection("users")
            .document(userId)
            .update("avatarUrl", avatarUrl)
            .await()
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
        val aggregate = db.collection("users")
            .document(userId)
            .collection("followers")
            .count()
            .get(AggregateSource.SERVER)
            .await()

        return aggregate.count.toInt()
    }



    override suspend fun getFollowingCount(userId: String): Int {
        val aggregate = db.collection("users")
            .document(userId)
            .collection("following")
            .count()
            .get(AggregateSource.SERVER)
            .await()

        return aggregate.count.toInt()
    }

    override suspend fun getUserConnections(
        userId: String,
        relationshipType: UserRelationshipListType
    ): List<UserDtoGeneric> {
        val connectedUserIds = getUserConnectionIds(userId, relationshipType)
        val usersById = connectedUserIds
            .chunked(UserRelationshipRules.MAX_USER_IDS_PER_QUERY)
            .flatMap { userIds ->
                db.collection("users")
                    .whereIn(FieldPath.documentId(), userIds)
                    .get()
                    .await()
                    .documents
                    .mapNotNull { document ->
                        document.toObject(UserFirestoreDto::class.java)
                            ?.copy(id = document.id)
                            ?.let { document.id to it }
                    }
            }
            .toMap()

        return connectedUserIds.mapNotNull(usersById::get)
    }

    override suspend fun getUserConnectionIds(
        userId: String,
        relationshipType: UserRelationshipListType
    ): List<String> {
        val collectionName = when (relationshipType) {
            UserRelationshipListType.FOLLOWERS -> "followers"
            UserRelationshipListType.FOLLOWING -> "following"
        }

        return db.collection("users")
            .document(userId)
            .collection(collectionName)
            .get()
            .await()
            .documents
            .map { it.id }
    }

    override suspend fun getNotificationActorEvents(
        userId: String,
        relationshipType: UserRelationshipListType
    ): List<NotificationActorEventDto> {
        val collectionName = when (relationshipType) {
            UserRelationshipListType.FOLLOWERS -> "followers"
            UserRelationshipListType.FOLLOWING -> "following"
        }

        return db.collection("users")
            .document(userId)
            .collection(collectionName)
            .get()
            .await()
            .documents
            .mapNotNull { document ->
                document.getTimestamp("timestamp")?.toDate()?.time?.let { timestamp ->
                    NotificationActorEventDto(
                        actorId = document.id,
                        timestampMillis = timestamp
                    )
                }
            }
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
