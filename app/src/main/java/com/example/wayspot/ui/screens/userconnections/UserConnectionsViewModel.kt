package com.example.wayspot.ui.screens.userconnections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.wayspot.data.model.user.UserRelationshipListType
import com.example.wayspot.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class UserConnectionsViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserConnectionsState())
    val uiState: StateFlow<UserConnectionsState> = _uiState
    private var activeRequestId = 0L

    fun loadConnections(
        userId: String,
        relationshipType: UserRelationshipListType
    ) {
        val currentState = _uiState.value
        val restoredType = if (savedStateHandle.get<String>(SAVED_USER_ID_KEY) == userId) {
            UserRelationshipListType.fromRoute(savedStateHandle.get<String>(SAVED_TYPE_KEY))
        } else {
            null
        }
        val selectedType = restoredType ?: relationshipType

        if (currentState.userId == userId && currentState.selectedType == selectedType &&
            (currentState.isLoading || currentState.hasLoaded) && !currentState.hasLoadError
        ) {
            return
        }

        val requestId = ++activeRequestId
        _uiState.value = UserConnectionsState(
            userId = userId,
            selectedType = selectedType,
            isLoading = true
        )
        loadPage(
            userId = userId,
            relationshipType = selectedType,
            cursor = null,
            append = false,
            requestId = requestId
        )
    }

    fun selectType(relationshipType: UserRelationshipListType) {
        val userId = _uiState.value.userId ?: return
        if (_uiState.value.selectedType == relationshipType) return
        savedStateHandle[SAVED_USER_ID_KEY] = userId
        savedStateHandle[SAVED_TYPE_KEY] = relationshipType.name
        loadConnections(userId, relationshipType)
    }

    fun retry() {
        val state = _uiState.value
        val userId = state.userId ?: return
        if (state.users.isNotEmpty() && state.nextCursor != null) {
            _uiState.update { it.copy(hasLoadError = false) }
            loadNextPage()
            return
        }
        loadConnections(userId, state.selectedType)
    }

    fun loadNextPage() {
        val state = _uiState.value
        val cursor = state.nextCursor ?: return
        val userId = state.userId ?: return
        if (state.isLoading || state.isLoadingMore || state.hasLoadError) return
        _uiState.update { it.copy(isLoadingMore = true) }
        loadPage(
            userId = userId,
            relationshipType = state.selectedType,
            cursor = cursor,
            append = true,
            requestId = activeRequestId
        )
    }

    private fun loadPage(
        userId: String,
        relationshipType: UserRelationshipListType,
        cursor: String?,
        append: Boolean,
        requestId: Long
    ) {
        viewModelScope.launch {
            try {
                val result = userRepository.getUserConnections(
                    userId = userId,
                    relationshipType = relationshipType,
                    cursor = cursor
                )
                result.fold(
                    onSuccess = { page ->
                        if (requestId != activeRequestId) return@fold
                        if (!append && page.users.isEmpty() && page.nextCursor != null) {
                            loadPage(
                                userId = userId,
                                relationshipType = relationshipType,
                                cursor = page.nextCursor,
                                append = false,
                                requestId = requestId
                            )
                            return@fold
                        }
                        _uiState.update { current ->
                            if (current.userId != userId ||
                                current.selectedType != relationshipType ||
                                requestId != activeRequestId
                            ) {
                                current
                            } else {
                                val users = if (append) {
                                    (current.users + page.users).distinctBy { it.id }
                                } else {
                                    page.users
                                }
                                current.copy(
                                    users = users,
                                    nextCursor = page.nextCursor,
                                    isLoading = false,
                                    isLoadingMore = false,
                                    hasLoadError = false,
                                    hasLoaded = true
                                )
                            }
                        }
                    },
                    onFailure = {
                        _uiState.update { current ->
                            if (current.userId == userId &&
                                current.selectedType == relationshipType &&
                                requestId == activeRequestId
                            ) {
                                current.copy(
                                    isLoading = false,
                                    isLoadingMore = false,
                                    hasLoadError = true,
                                    hasLoaded = true
                                )
                            } else {
                                current
                            }
                        }
                    }
                )
            } catch (exception: CancellationException) {
                throw exception
            }
        }
    }

    private companion object {
        const val SAVED_USER_ID_KEY = "userConnections.userId"
        const val SAVED_TYPE_KEY = "userConnections.type"
    }
}
