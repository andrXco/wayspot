package com.example.wayspot.data.model.review

import com.example.wayspot.data.model.place.PlaceInfo
import com.example.wayspot.data.model.user.UserInfo

data class ReviewFeedItem(
    val review: ReviewInfo,
    val place: PlaceInfo,
    val author: UserInfo
)
