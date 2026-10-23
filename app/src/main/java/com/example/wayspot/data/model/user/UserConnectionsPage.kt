package com.example.wayspot.data.model.user

/** Página inmutable de personas relacionadas con la cuenta consultada. */
data class UserConnectionsPage(
    val users: List<UserInfo>,
    val nextCursor: String?
)
