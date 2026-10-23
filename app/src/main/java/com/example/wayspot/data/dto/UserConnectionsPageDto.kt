package com.example.wayspot.data.dto

/** Página remota de relaciones y el ID desde el que puede continuarse la consulta. */
data class UserConnectionsPageDto(
    val users: List<UserDtoGeneric>,
    val nextCursor: String?
)
