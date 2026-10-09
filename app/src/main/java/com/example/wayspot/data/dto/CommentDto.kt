package com.example.wayspot.data.dto

import com.example.wayspot.data.model.ReviewComment
import com.example.wayspot.data.dto.toUserInfo

data class CommentDto(
    val id: Int,
    val content: String,
    val reviewId: Int,
    val userId: Int,
    val createdAt: String,
    val user: UserDto
)

data class CreateCommentDto(val content: String, val userId: Int)

fun CommentDto.toReviewComment() = ReviewComment(
    id = id.toString(),
    content = content,
    reviewId = reviewId.toString(),
    author = user.toUserInfo()
)
