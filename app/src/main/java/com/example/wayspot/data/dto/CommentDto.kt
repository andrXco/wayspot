package com.example.wayspot.data.dto

data class CommentDto(
    val id: Int,
    val content: String,
    val reviewId: Int,
    val userId: Int,
    val createdAt: String,
    val user: CommentAuthorDto
)

data class CommentAuthorDto(
    val id: Int,
    val name: String,
    val username: String,
    val avatarUrl: String?
)

data class CreateCommentDto(val content: String, val userId: Int)
