package com.example.wayspot.ui.screens.reviewcomment

import com.example.wayspot.data.model.ReviewCommentRules

/** Estado renderizable del editor para publicar un comentario en una reseña. */
data class ReviewCommentState(
    val reviewId: String? = null,
    val draft: String = "",
    val isPublishing: Boolean = false,
    val isPublished: Boolean = false,
    val errorMessage: String? = null
) {
    val canPublish: Boolean
        get() = reviewId != null && ReviewCommentRules.canPublish(draft) && !isPublishing
}
