
package com.example.wayspot.data.dto

import com.example.wayspot.data.mapper.toUserInfo
import com.example.wayspot.data.model.review.ReviewComment

data class CommentDto(
    val id: Int,
    val content: String,
    val reviewId: Int,
    val userId: Int,
    val createdAt: String,
    val user: CommentAuthorDto
) : CommentDtoGeneric() {

    override fun toReviewComment(): ReviewComment {
        return ReviewComment(
            id = id.toString(),
            content = content,
            reviewId = reviewId.toString(),
            author = user.toUserInfo()
        )
    }
}

data class CommentAuthorDto(
    val id: Int,
    val name: String,
    val username: String,
    val avatarUrl: String?
)

data class CreateCommentDto(
    val content: String,
    val userId: String,
    val user: CommentFirestoreUserDto? = null
)
