package com.example.wayspot.ui.screens.newreview

import com.example.wayspot.data.dto.CommentAuthorDto
import com.example.wayspot.data.dto.CommentDto
import com.example.wayspot.data.dto.PlaceDto
import com.example.wayspot.data.dto.ReviewDto
import com.example.wayspot.data.dto.UserDto
import com.example.wayspot.data.dto.toPlaceInfo
import com.example.wayspot.data.dto.toReviewComment
import com.example.wayspot.data.dto.toReviewInfo
import com.example.wayspot.data.dto.toUserInfo
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
    fun userDtoMapsBackendIdToDomainId() {
        val user = UserDto(2, "Ana", "ana", "ana@wayspot.com", null, null, null).toUserInfo()

        assertEquals("2", user.id)
        assertEquals("Ana", user.name)
    }

    @Test
    fun commentDtoMapsItsAuthor() {
        val author = CommentAuthorDto(2, "Ana", "ana", null)
        val comment = CommentDto(5, "De acuerdo", 12, 2, "2026-10-09T12:00:00.000Z", author)
            .toReviewComment()

        assertEquals("12", comment.reviewId)
        assertEquals("2", comment.author.id)
        assertEquals("Ana", comment.author.name)
    }

    @Test
    fun placeDtoMapsNullDescriptionToEmptyText() {
        val place = PlaceDto(7, "Mirador", "Naturaleza", "Bogotá", 4.5, null, null)

        assertEquals("", place.toPlaceInfo().description)
    }

    @Test
    fun reviewDraftRequiresTitleAndDescription() {
        val empty = ReviewRules.emptyDraft("7")
        assertFalse(ReviewRules.canPublish(empty))
        assertTrue(ReviewRules.canPublish(empty.copy(title = "Visita", description = "Muy bien")))
    }
}
