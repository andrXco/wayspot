package com.example.wayspot.ui.screens.userconnections

import com.example.wayspot.data.model.user.UserInfo
import com.example.wayspot.data.model.user.UserRelationshipListType

data class UserConnectionsState(
    val userId: String? = null,
    val selectedType: UserRelationshipListType = UserRelationshipListType.FOLLOWERS,
    val users: List<UserInfo> = emptyList(),
    val isLoading: Boolean = false,
    val hasLoadError: Boolean = false,
    val hasLoaded: Boolean = false
)
