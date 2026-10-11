package com.example.wayspot.data.dto

import com.example.wayspot.data.model.ReviewComment
import com.example.wayspot.data.model.UserInfo

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

fun CommentDto.toReviewComment() = ReviewComment(
    id = id.toString(),
    content = content,
    reviewId = reviewId.toString(),
    author = user.toUserInfo()
)

fun CommentAuthorDto.toUserInfo() = UserInfo(
    id = id.toString(),
    name = name,
    username = username,
    bio = null,
    location = null,
    avatarUrl = avatarUrl
)
