package com.example.wayspot.data.dto

import com.example.wayspot.data.model.review.ReviewComment

abstract class CommentDtoGeneric {

    abstract fun toReviewComment(): ReviewComment

}
