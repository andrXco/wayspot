package com.example.wayspot.data.dto

import com.example.wayspot.data.model.review.ReviewInfo

abstract class ReviewDtoGeneric {

    abstract fun toReviewInfo(): ReviewInfo

}
