package com.example.wayspot.data.model

object ReviewRules {
    const val MIN_RATING = 1
    const val MAX_RATING = 5
    const val DEFAULT_RATING = 2
    const val MAX_TITLE_LENGTH = 80
    const val MAX_DESCRIPTION_LENGTH = 500
    const val MAX_PHOTOS = 3

    fun emptyDraft(placeId: String): ReviewDraft = ReviewDraft(
        placeId = placeId,
        rating = DEFAULT_RATING,
        title = "",
        description = "",
        photoUris = emptyList()
    )

    fun normalizeDraft(
        draft: ReviewDraft,
        placeId: String = draft.placeId
    ): ReviewDraft = draft.copy(
        placeId = placeId,
        rating = draft.rating.coerceIn(MIN_RATING, MAX_RATING),
        title = draft.title.take(MAX_TITLE_LENGTH),
        description = draft.description.take(MAX_DESCRIPTION_LENGTH),
        photoUris = draft.photoUris
            .distinct()
            .take(MAX_PHOTOS)
            .toList()
    )

    fun canPublish(
        rating: Int,
        title: String,
        description: String
    ): Boolean = rating in MIN_RATING..MAX_RATING &&
        title.isNotBlank() &&
        description.isNotBlank()
}
