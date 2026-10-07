package com.example.wayspot.ui.screens.newreview

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.wayspot.data.local.PreviewData
import com.example.wayspot.data.local.PreviewDataPopular
import com.example.wayspot.data.model.PlaceInfo
import com.example.wayspot.data.model.ReviewDraft
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.screens.newreview.components.NewReviewHeader
import com.example.wayspot.ui.screens.newreview.components.ReviewBottomAction
import com.example.wayspot.ui.screens.newreview.components.ReviewEditorSection
import com.example.wayspot.ui.screens.newreview.components.ReviewPlaceSummaryCard
import com.example.wayspot.ui.theme.WayspotTheme

/** Conecta el borrador con el editor y coordina la selección de fotografías del sistema. */
@Composable
fun NewReviewScreen(
    newReviewViewModel: NewReviewViewModel,
    placeId: String?,
    reviewId: String?,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    onPublishReview: () -> Unit = {}
) {
    val state by newReviewViewModel.uiState.collectAsState()

    LaunchedEffect(placeId, reviewId) {
        newReviewViewModel.loadReview(placeId, reviewId)
    }

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) {
            newReviewViewModel.consumeSaved()
            onPublishReview()
        }
    }

    BackHandler(
        onBack = onBackClick
    )

    NewReviewContent(
        places = state.places,
        selectedPlace = state.place,
        reviewDraft = state.reviewDraft,

        onPlaceSelected = {
            newReviewViewModel.selectPlace(it)
        },

        onRatingSelected = {
            newReviewViewModel.updateRating(it)
        },

        onTitleChange = {
            newReviewViewModel.updateTitle(it)
        },

        onDescriptionChange = {
            newReviewViewModel.updateDescription(it)
        },

        isPublishEnabled = state.isPublishEnabled && !state.isPublishing,
        isEditing = state.editingReviewId != null,
        isLoading = state.isLoading,
        errorMessage = state.errorMessage ?: state.errorResId?.let { stringResource(it) },
        onBackClick = onBackClick,
        onPublishClick = newReviewViewModel::publish,

        modifier = modifier
    )
}

/** Presenta el editor de reseña como composición sin estado de encabezado, contenido y acción final. */
@Composable
fun NewReviewContent(
    places: List<PlaceInfo>,
    selectedPlace: PlaceInfo?,
    reviewDraft: ReviewDraft?,
    onPlaceSelected: (String) -> Unit,
    onRatingSelected: (Int) -> Unit,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    isPublishEnabled: Boolean,
    isEditing: Boolean,
    isLoading: Boolean,
    errorMessage: String?,
    onBackClick: () -> Unit,
    onPublishClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .imePadding()
    ) {
        NewReviewHeader(
            onBackClick = onBackClick,
            modifier = Modifier.fillMaxWidth(),
            isEditing = isEditing
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(
                    horizontal = 16.dp,
                    vertical = 16.dp
                )
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            }
            errorMessage?.let { message ->
                Text(text = message, color = MaterialTheme.colorScheme.error)
            }
            ReviewPlaceSummaryCard(
                places = places,
                selectedPlace = selectedPlace,
                onPlaceSelected = onPlaceSelected,
                modifier = Modifier.fillMaxWidth()
            )

            if (selectedPlace != null && reviewDraft != null) {
                ReviewEditorSection(
                    reviewDraft = reviewDraft,
                    onRatingSelected = onRatingSelected,
                    onTitleChange = onTitleChange,
                    onDescriptionChange = onDescriptionChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp)
                )
            }
        }

        ReviewBottomAction(
            isEnabled = isPublishEnabled,
            isEditing = isEditing,
            onPublishClick = onPublishClick,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@WayspotMultiPreview
@Composable
private fun NewReviewScreenPreview() {
    val reviewDraft = PreviewData.newReviewDraft

    WayspotTheme {
        NewReviewContent(
            places = listOf(PreviewDataPopular.previewPlaceInfo),
            selectedPlace = PreviewDataPopular.previewPlaceInfo,
            reviewDraft = reviewDraft,
            onPlaceSelected = {},
            onRatingSelected = {},
            onTitleChange = {},
            onDescriptionChange = {},
            isPublishEnabled = false,
            isEditing = false,
            isLoading = false,
            errorMessage = null,
            onBackClick = {},
            onPublishClick = {}
        )
    }
}
