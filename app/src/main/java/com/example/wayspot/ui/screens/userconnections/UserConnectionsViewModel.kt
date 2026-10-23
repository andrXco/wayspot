package com.example.wayspot.ui.screens.userconnections

import androidx.lifecycle.ViewModel
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
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserConnectionsState())
    val uiState: StateFlow<UserConnectionsState> = _uiState
    private var activeRequestId = 0L

    fun loadConnections(
        userId: String,
        relationshipType: UserRelationshipListType
    ) {
        val currentState = _uiState.value
        if (currentState.userId == userId && currentState.selectedType == relationshipType &&
            (currentState.isLoading || currentState.hasLoaded) && !currentState.hasLoadError
        ) {
            return
        }

        val requestId = ++activeRequestId
        _uiState.value = UserConnectionsState(
            userId = userId,
            selectedType = relationshipType,
            isLoading = true
        )
        loadConnections(userId, relationshipType, requestId)
    }

    fun selectType(relationshipType: UserRelationshipListType) {
        val userId = _uiState.value.userId ?: return
        if (_uiState.value.selectedType == relationshipType) return
        loadConnections(userId, relationshipType)
    }

    fun retry() {
        val state = _uiState.value
        val userId = state.userId ?: return
        loadConnections(userId, state.selectedType)
    }

    private fun loadConnections(
        userId: String,
        relationshipType: UserRelationshipListType,
        requestId: Long
    ) {
        viewModelScope.launch {
            try {
                val result = userRepository.getUserConnections(
                    userId = userId,
                    relationshipType = relationshipType
                )
                result.fold(
                    onSuccess = { users ->
                        if (requestId != activeRequestId) return@fold
                        _uiState.update { current ->
                            if (current.userId != userId ||
                                current.selectedType != relationshipType ||
                                requestId != activeRequestId
                            ) {
                                current
                            } else {
                                current.copy(
                                    users = users,
                                    isLoading = false,
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

}
