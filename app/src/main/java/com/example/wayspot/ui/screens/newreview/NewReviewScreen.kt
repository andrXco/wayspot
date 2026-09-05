package com.example.wayspot.ui.screens.newreview

import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.wayspot.data.local.PreviewData
import com.example.wayspot.data.local.PreviewDataPopular
import com.example.wayspot.data.model.Place
import com.example.wayspot.data.model.ReviewDraft
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.screens.newreview.components.NewReviewHeader
import com.example.wayspot.ui.screens.newreview.components.ReviewBottomAction
import com.example.wayspot.ui.screens.newreview.components.ReviewEditorSection
import com.example.wayspot.ui.theme.WayspotTheme

@Composable
fun NewReviewScreen(
    newReviewViewModel: NewReviewViewModel,
    placeId: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    initialDraft: ReviewDraft? = null,
    onSaveDraft: (ReviewDraft) -> Unit = {},
    onPublishReview: (ReviewDraft) -> Unit = {}
) {
    val state by newReviewViewModel.uiState.collectAsState()

    LaunchedEffect(placeId, initialDraft) {
        newReviewViewModel.loadReview(
            placeId = placeId,
            initialDraft = initialDraft
        )
    }

    val place = state.place ?: return
    val reviewDraft = state.reviewDraft ?: return

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { selectedUri ->

        val selectedUriString = selectedUri?.toString()

        if (selectedUriString != null) {
            newReviewViewModel.addPhoto(
                selectedUriString
            )
        }
    }

    BackHandler(
        onBack = onBackClick
    )

    NewReviewContent(
        place = place,
        reviewDraft = reviewDraft,

        onRatingSelected = {
            newReviewViewModel.updateRating(it)
        },

        onTitleChange = {
            newReviewViewModel.updateTitle(it)
        },

        onDescriptionChange = {
            newReviewViewModel.updateDescription(it)
        },

        onAddPhotosClick = {
            photoPickerLauncher.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
        },

        onRemovePhotoClick = {
            newReviewViewModel.removePhoto(it)
        },

        isPublishEnabled = state.isPublishEnabled,
        onBackClick = onBackClick,

        onSaveDraftClick = {
            newReviewViewModel.draftForSaving()?.let(onSaveDraft)
        },

        onPublishClick = {
            newReviewViewModel.draftForPublishing()?.let(onPublishReview)
        },

        modifier = modifier
    )
}

@Composable
fun NewReviewContent(
    place: Place,
    reviewDraft: ReviewDraft,
    onRatingSelected: (Int) -> Unit,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onAddPhotosClick: () -> Unit,
    onRemovePhotoClick: (String) -> Unit,
    isPublishEnabled: Boolean,
    onBackClick: () -> Unit,
    onSaveDraftClick: () -> Unit,
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
            onSaveDraftClick = onSaveDraftClick,
            modifier = Modifier.fillMaxWidth()
        )

        ReviewEditorSection(
            place = place,
            reviewDraft = reviewDraft,
            onRatingSelected = onRatingSelected,
            onTitleChange = onTitleChange,
            onDescriptionChange = onDescriptionChange,
            onAddPhotosClick = onAddPhotosClick,
            onRemovePhotoClick = onRemovePhotoClick,
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
        )

        ReviewBottomAction(
            isEnabled = isPublishEnabled,
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
            place = PreviewDataPopular.samplePlaces1,
            reviewDraft = reviewDraft,
            onRatingSelected = {},
            onTitleChange = {},
            onDescriptionChange = {},
            onAddPhotosClick = {},
            onRemovePhotoClick = {},
            isPublishEnabled = false,
            onBackClick = {},
            onSaveDraftClick = {},
            onPublishClick = {}
        )
    }
}
