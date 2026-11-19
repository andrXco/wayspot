package com.example.wayspot.achievement

import com.example.wayspot.data.model.achievement.AchievementRules
import com.example.wayspot.data.model.achievement.AchievementType
import com.example.wayspot.data.model.review.ReviewInfo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AchievementRulesTest {
    private fun review(
        id: String,
        placeId: String,
        likes: Int = 0,
        photos: List<String> = emptyList()
    ) = ReviewInfo(
        id = id,
        rating = 5,
        title = null,
        description = "Prueba",
        userId = "usuario",
        placeId = placeId,
        likesCount = likes,
        photoUrls = photos
    )

    @Test
    fun `sin reseñas ninguna insignia esta desbloqueada`() {
        val progress = AchievementRules.calculate(emptyList())
        assertEquals(5, progress.size)
        assertTrue(progress.all { it.current == 0 && !it.isUnlocked })
    }

    @Test
    fun `una reseña desbloquea primer paso`() {
        val progress = AchievementRules.calculate(listOf(review("r1", "p1")))
        assertTrue(progress.first { it.type == AchievementType.FIRST_REVIEW }.isUnlocked)
        assertFalse(progress.first { it.type == AchievementType.ACTIVE_EXPLORER }.isUnlocked)
    }

    @Test
    fun `lugares distintos y fotos se cuentan por reseña`() {
        val progress = AchievementRules.calculate(
            listOf(
                review("r1", "p1", photos = listOf("foto1")),
                review("r2", "p1", photos = listOf("foto2")),
                review("r3", "p2", photos = listOf("foto3")),
                review("r4", "p3")
            )
        )
        assertEquals(3, progress.first { it.type == AchievementType.ADVENTURER }.current)
        assertTrue(progress.first { it.type == AchievementType.ADVENTURER }.isUnlocked)
        assertEquals(3, progress.first { it.type == AchievementType.PHOTOGRAPHER }.current)
        assertTrue(progress.first { it.type == AchievementType.PHOTOGRAPHER }.isUnlocked)
    }

    @Test
    fun `likes acumulados nunca incluyen negativos`() {
        val progress = AchievementRules.calculate(
            listOf(review("r1", "p1", likes = 11), review("r2", "p2", likes = -5))
        )
        val influencer = progress.first { it.type == AchievementType.INFLUENCER }
        assertEquals(11, influencer.current)
        assertTrue(influencer.isUnlocked)
        assertEquals(1f, influencer.fraction)
    }
}
