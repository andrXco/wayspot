package com.example.wayspot.data.mapper

import com.example.wayspot.data.dto.CommentAuthorDto
import com.example.wayspot.data.dto.CommentDto
import com.example.wayspot.data.dto.PlaceDto
import com.example.wayspot.data.model.place.Place
import com.example.wayspot.data.model.place.PlaceDetail
import com.example.wayspot.data.model.place.PlaceInfo
import com.example.wayspot.data.model.review.ReviewComment
import com.example.wayspot.data.model.user.UserInfo

fun PlaceDto.toPlaceInfo() = PlaceInfo(
    id = id.toString(),
    title = title,
    category = category,
    location = location,
    rating = rating,
    imageUrl = imageUrl,
    description = description ?: "",
    slug = slug ?: id.toString(),
    duration = duration,
    price = price,
    altitude = altitude,
    reviewCount = reviewCount
)

fun PlaceDto.toPlace() = Place(
    id = slug ?: id.toString(),
    tituloRes = 0,
    categoriaRes = 0,
    ubicacionRes = 0,
    rating = rating,
    imagen = imageUrl,
    detail = PlaceDetail(
        durationRes = 0,
        priceRes = 0,
        altitudeRes = 0,
        descriptionRes = 0,
        reviewCount = reviewCount,
        recentReviews = emptyList(),
        durationText = duration,
        priceText = price,
        altitudeText = altitude,
        descriptionText = description
    ),
    titleText = title,
    categoryText = category,
    locationText = location
)

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