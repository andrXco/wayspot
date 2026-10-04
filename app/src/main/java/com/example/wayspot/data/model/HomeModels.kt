package com.example.wayspot.data.model

import androidx.annotation.StringRes
import java.text.Normalizer

/** Identificadores estables de las categorías que organizan el contenido destacado de inicio. */
enum class HomeCategoryId {
    NATURE,
    GASTRONOMY,
    GETAWAYS,
    CULTURE,
    ADVENTURE
}

/** Datos de presentación de una categoría de inicio. */
data class HomeCategory(
    val id: HomeCategoryId,
    @param:StringRes val labelRes: Int
)

/** Vínculo entre un lugar destacado, su categoría y sus métricas iniciales de interacción. */
data class HomeFeaturedPlan(
    val id: String,
    val placeId: String,
    val categoryId: HomeCategoryId,
    @param:StringRes val badgeRes: Int,
    val initialLikeCount: Int
)

/** Reseña localizada que se muestra dentro del feed de inicio. */
data class HomeReview(
    val id: String,
    val placeId: String,
    @param:StringRes val authorNameRes: Int,
    @param:StringRes val authorHandleRes: Int,
    @param:StringRes val authorInitialsRes: Int,
    @param:StringRes val dateRes: Int,
    @param:StringRes val bodyRes: Int,
    val rating: Int,
    val photoUrls: List<String>,
    val initialLikeCount: Int,
    val initialCommentCount: Int
)

/** Reglas puras compartidas para filtrar, navegar y normalizar datos del inicio. */
object HomeRules {
    const val MAX_COLLAPSED_REVIEW_LINES = 4
    const val MIN_REVIEW_RATING = 1
    const val MAX_REVIEW_RATING = 5

    fun filterFeaturedPlans(
        plans: List<HomeFeaturedPlan>,
        categoryId: HomeCategoryId
    ): List<HomeFeaturedPlan> = plans.filter { plan ->
        plan.categoryId == categoryId
    }

    fun nextCircularIndex(currentIndex: Int, size: Int): Int = circularIndex(
        currentIndex = currentIndex,
        size = size,
        step = 1
    )

    fun previousCircularIndex(currentIndex: Int, size: Int): Int = circularIndex(
        currentIndex = currentIndex,
        size = size,
        step = -1
    )

    fun matchesSearch(query: String, candidates: List<String>): Boolean {
        val normalizedQuery = query.normalizedForSearch()
        return normalizedQuery.isBlank() || candidates.any { candidate ->
            candidate.normalizedForSearch().contains(normalizedQuery)
        }
    }

    fun normalizedReviewRating(rating: Int): Int = rating.coerceIn(
        minimumValue = MIN_REVIEW_RATING,
        maximumValue = MAX_REVIEW_RATING
    )

    private fun circularIndex(
        currentIndex: Int,
        size: Int,
        step: Int
    ): Int = if (size <= 0) {
        0
    } else {
        ((currentIndex + step) % size + size) % size
    }

    private fun String.normalizedForSearch(): String = Normalizer
        .normalize(trim().lowercase(), Normalizer.Form.NFD)
        .replace(Regex("\\p{Mn}+"), "")
}
