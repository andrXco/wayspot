package com.example.wayspot.ui.screens.publicprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wayspot.data.repository.AuthRepository
import com.example.wayspot.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class PublicProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {


    private val _uiState = MutableStateFlow(PublicProfileState())
    val uiState: StateFlow<PublicProfileState> = _uiState

    fun loadUser(id: String) {
        val currentUserId = authRepository.currentUser?.uid
        _uiState.update {
            it.copy(
                isLoading = true,
                errorMessage = null,
                hasFollowError = false,
                user = null,
                reviews = emptyList(),
                canFollow = currentUserId != null && currentUserId != id,
                isFollowing = false,
                followersCount = 0,
                followingCount = 0
            )
        }
        viewModelScope.launch {
            val user = userRepository.getUserById(id)
            if (user.isFailure) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = user.exceptionOrNull()?.message
                    )
                }
                return@launch
            }
            val reviews = userRepository.getReviewsByUserId(id)
            val isFollowing = currentUserId
                ?.takeIf { it != id }
                ?.let { userRepository.isFollowing(it, id).getOrNull() }
                ?: false
            val followersCount = userRepository.getFollowersCount(id).getOrNull() ?: 0
            val followingCount = userRepository.getFollowingCount(id).getOrNull() ?: 0
            _uiState.update {
                it.copy(
                    user = user.getOrNull(),
                    reviews = reviews.getOrNull().orEmpty(),
                    isFollowing = isFollowing,
                    followersCount = followersCount,
                    followingCount = followingCount,
                    isLoading = false,
                    errorMessage = reviews.exceptionOrNull()?.message
                )
            }
        }
    }

    fun toggleFollow() {
        val state = _uiState.value
        val followerId = authRepository.currentUser?.uid ?: return
        val followedId = state.user?.id ?: return
        if (!state.canFollow || state.isFollowActionInProgress || followedId.isBlank()) return

        val nextFollowingState = !state.isFollowing
        _uiState.update {
            it.copy(
                isFollowActionInProgress = true,
                hasFollowError = false
            )
        }

        viewModelScope.launch {
            val result = userRepository.followOrUnfollowUser(
                currentUserId = followerId,
                targetUserId = followedId
            )
            if (result.isSuccess) {
                val refreshedCount = userRepository.getFollowersCount(followedId).getOrNull()
                _uiState.update {
                    it.copy(
                        isFollowing = nextFollowingState,
                        followersCount = refreshedCount
                            ?: (it.followersCount + if (nextFollowingState) 1 else -1)
                                .coerceAtLeast(0),
                        isFollowActionInProgress = false
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isFollowActionInProgress = false,
                        hasFollowError = true
                    )
                }
            }
        }
    }
}
