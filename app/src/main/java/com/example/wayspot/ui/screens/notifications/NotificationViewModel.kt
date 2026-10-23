package com.example.wayspot.ui.screens.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wayspot.data.model.notification.Notification
import com.example.wayspot.data.model.notification.NotificationType
import com.example.wayspot.data.repository.AuthRepository
import com.example.wayspot.data.repository.ReviewRepository
import com.example.wayspot.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationsState())
    val uiState: StateFlow<NotificationsState> = _uiState

    fun loadNotifications() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, hasLoadError = false) }
            try {
                val userId = authRepository.currentUser?.uid
                if (userId == null) {
                    _uiState.update { it.copy(notifications = emptyList(), isLoading = false) }
                    return@launch
                }
                val profile = userRepository.getUserById(userId).getOrThrow()
                val followerEvents = if (profile.notificationPreferences.newFollowers) {
                    userRepository.getFollowerNotificationEvents(userId).getOrThrow()
                } else emptyList()
                val reviews = userRepository.getReviewsByUserId(userId).getOrThrow()
                val likeEvents = if (profile.notificationPreferences.likesReceived) {
                    reviewRepository.getReviewLikeNotificationEvents(reviews.map { it.id }).getOrThrow()
                } else emptyMap()
                val actorIds = (followerEvents.map { it.actorId } +
                    likeEvents.values.flatten().map { it.actorId }).distinct()
                val actors = userRepository.getUsersByIds(actorIds).getOrThrow().associateBy { it.id }
                val reviewsById = reviews.associateBy { it.id }
                val notifications = buildList {
                    followerEvents.forEach { event ->
                        val actor = actors[event.actorId] ?: return@forEach
                        add(Notification(
                            id = "follow:${event.actorId}",
                            username = actor.name.ifBlank { actor.username },
                            type = NotificationType.FOLLOW,
                            detail = "",
                            timestampMillis = event.timestampMillis,
                            avatarUrl = actor.avatarUrl
                        ))
                    }
                    likeEvents.forEach { (reviewId, events) ->
                        val review = reviewsById[reviewId] ?: return@forEach
                        events.forEach eventLoop@{ event ->
                            val actor = actors[event.actorId] ?: return@eventLoop
                            add(Notification(
                                id = "like:$reviewId:${event.actorId}",
                                username = actor.name.ifBlank { actor.username },
                                type = NotificationType.REVIEW_LIKE,
                                detail = review.title.orEmpty(),
                                timestampMillis = event.timestampMillis,
                                avatarUrl = actor.avatarUrl
                            ))
                        }
                    }
                }.sortedByDescending { it.timestampMillis }
                _uiState.update {
                    it.copy(notifications = notifications, isLoading = false, hasLoadError = false)
                }
            } catch (e: CancellationException) {
                throw e
            } catch (_: Exception) {
                _uiState.update { it.copy(isLoading = false, hasLoadError = true) }
            }
        }
    }
}
