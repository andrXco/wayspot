package com.example.wayspot.repository

import com.example.wayspot.data.model.review.RecentReviewWindow
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RecentReviewWindowTest {
    private val nowMillis = 1_700_000_000_000L

    // Comprueba que la ventana incluye publicaciones recientes y excluye las vencidas.
    @Test
    fun contains_onlyReviewsWithinLast24Hours() {
        val cutoff = RecentReviewWindow.cutoffMillis(nowMillis)

        assertThat(RecentReviewWindow.contains(cutoff, nowMillis)).isFalse()
        assertThat(RecentReviewWindow.contains(cutoff + 1L, nowMillis)).isTrue()
        assertThat(RecentReviewWindow.contains(nowMillis, nowMillis)).isTrue()
        assertThat(RecentReviewWindow.contains(nowMillis + 1L, nowMillis)).isFalse()
        assertThat(RecentReviewWindow.contains(null, nowMillis)).isFalse()
    }

    // Comprueba el instante usado para retirar un marcador sin esperar otro evento remoto.
    @Test
    fun expiresAtMillis_isFirstInstantOutsideWindow() {
        val createdAt = nowMillis - RecentReviewWindow.DURATION_MILLIS + 1L

        assertThat(RecentReviewWindow.contains(createdAt, nowMillis)).isTrue()
        assertThat(RecentReviewWindow.contains(createdAt, RecentReviewWindow.expiresAtMillis(createdAt)))
            .isFalse()
    }
}
