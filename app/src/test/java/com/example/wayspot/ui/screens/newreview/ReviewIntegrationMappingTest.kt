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
        val author = UserDto(2, "Ana", "ana", null, null, null)
        val comment = CommentDto(5, "De acuerdo", 12, 2, author).toReviewComment()

        assertEquals("12", comment.reviewId)
        assertEquals(2, comment.author.id)
        assertEquals("Ana", comment.author.name)
    }

    @Test
    fun reviewDraftRequiresTitleAndDescription() {
        val empty = ReviewRules.emptyDraft("7")
        assertFalse(ReviewRules.canPublish(empty))
        assertTrue(ReviewRules.canPublish(empty.copy(title = "Visita", description = "Muy bien")))
    }
}
