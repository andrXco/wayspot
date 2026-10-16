
package com.example.wayspot.data.dto

import com.example.wayspot.data.model.review.ReviewComment
import com.example.wayspot.data.model.user.UserInfo


data class CommentFirestoreUserDto(
    val name: String? = null,
    val username: String? = null,
    val avatarUrl: String? = null
)


data class CreateCommentFirestoreDto(
    val content: String,
    val reviewId: String,
    val userId: String,
    val user: CommentFirestoreUserDto? = null
)

data class CommentFirestoreDto(
    val id: String = "",
    val content: String = "",
    val reviewId: String = "",
    val userId: String = "",
    val user: CommentFirestoreUserDto? = null
) : CommentDtoGeneric() {

    override fun toReviewComment(): ReviewComment {

        return ReviewComment(
            id = id,
            content = content,
            reviewId = reviewId,
            author = UserInfo(
                id = userId,
                name = user?.name.orEmpty(),
                username = user?.username.orEmpty(),
                bio = null,
                location = null,
                avatarUrl = user?.avatarUrl
            )
        )
    }
}
