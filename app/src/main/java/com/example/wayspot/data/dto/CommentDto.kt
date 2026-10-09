package com.example.wayspot.data.dto

import com.example.wayspot.data.model.ReviewComment

data class CommentDto(
    val id: Int,
    val content: String,
    val reviewId: Int,
    val userId: Int,
    val user: UserDto
)

data class CreateCommentDto(val content: String, val userId: Int)

fun CommentDto.toReviewComment() = ReviewComment(
    id = id.toString(),
    content = content,
    reviewId = reviewId.toString(),
    author = user
)
