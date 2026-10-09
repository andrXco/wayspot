package com.example.wayspot.ui.screens.newreview

import com.example.wayspot.data.dto.CommentDto
import com.example.wayspot.data.dto.ReviewDto
import com.example.wayspot.data.dto.UserDto
import com.example.wayspot.data.dto.toReviewComment
import com.example.wayspot.data.dto.toReviewInfo
import com.example.wayspot.data.model.ReviewRules
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ReviewIntegrationMappingTest {
    @Test
    fun reviewDtoKeepsBackendIds() {
        val info = ReviewDto(12, 4, "Visita", "Muy bien", 1, 7).toReviewInfo()

        assertEquals("12", info.id)
        assertEquals("1", info.userId)
        assertEquals("7", info.placeId)
    }

    @Test
    fun commentDtoMapsItsAuthor() {
        val author = UserDto(
            id = 2,
            name = "Ana",
            username = "ana",
            email = "ana@wayspot.com",
            bio = null,
            location = null,
            avatarUrl = null
        )

        val comment = CommentDto(
            id = 5,
            content = "De acuerdo",
            reviewId = 12,
            userId = 2,
            createdAt = "2026-10-09T10:00:00.000Z",
            user = author
        ).toReviewComment()

        assertEquals("12", comment.reviewId)
        assertEquals("2", comment.author.id)
        assertEquals("Ana", comment.author.name)
    }

    @Test
    fun reviewDraftRequiresTitleAndDescription() {
        val empty = ReviewRules.emptyDraft("7")
        assertFalse(ReviewRules.canPublish(empty))
        assertTrue(ReviewRules.canPublish(empty.copy(title = "Visita", description = "Muy bien")))
    }
}
