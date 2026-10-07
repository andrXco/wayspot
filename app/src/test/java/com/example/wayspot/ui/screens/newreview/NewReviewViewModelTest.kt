package com.example.wayspot.ui.screens.newreview

import com.example.wayspot.data.local.PreviewDataPopular
import com.example.wayspot.data.model.ReviewRules
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotSame
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Test

class NewReviewViewModelTest {

    @Test
    fun loadAndSelectionFlowMaintainsDraftContract() {
        val viewModel = NewReviewViewModel()
        val places = PreviewDataPopular.listPlaces
        val firstPlace = places[0]
        val secondPlace = places[1]

        viewModel.loadReview(null)

        assertEquals(places, viewModel.uiState.value.places)
        assertNull(viewModel.uiState.value.place)
        assertNull(viewModel.uiState.value.reviewDraft)

        viewModel.loadReview(firstPlace.id)

        assertEquals(firstPlace, viewModel.uiState.value.place)
        assertEquals(
            ReviewRules.emptyDraft(firstPlace.id),
            viewModel.uiState.value.reviewDraft
        )

        viewModel.updateTitle("Título conservado")
        val editedDraft = viewModel.uiState.value.reviewDraft

        viewModel.loadReview(null)

        assertSame(editedDraft, viewModel.uiState.value.reviewDraft)
        assertEquals(firstPlace, viewModel.uiState.value.place)

        viewModel.selectPlace(firstPlace.id)

        assertSame(editedDraft, viewModel.uiState.value.reviewDraft)

        viewModel.selectPlace(secondPlace.id)

        assertNotSame(editedDraft, viewModel.uiState.value.reviewDraft)
        assertEquals(secondPlace, viewModel.uiState.value.place)
        assertEquals(
            ReviewRules.emptyDraft(secondPlace.id),
            viewModel.uiState.value.reviewDraft
        )

        val validState = viewModel.uiState.value

        viewModel.selectPlace("missing_place")

        assertSame(validState, viewModel.uiState.value)
    }
}
