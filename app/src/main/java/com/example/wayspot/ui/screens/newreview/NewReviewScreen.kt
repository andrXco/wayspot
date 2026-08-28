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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.wayspot.data.local.PreviewData
import com.example.wayspot.data.local.PreviewDataPopular
import com.example.wayspot.data.model.Place
import com.example.wayspot.data.model.ReviewDraft
import com.example.wayspot.data.model.ReviewRules
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.screens.newreview.components.NewReviewHeader
import com.example.wayspot.ui.screens.newreview.components.ReviewBottomAction
import com.example.wayspot.ui.screens.newreview.components.ReviewEditorSection
import com.example.wayspot.ui.theme.WayspotTheme

@Composable
fun NewReviewScreen(
    placeId: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    initialDraft: ReviewDraft? = null,
    onSaveDraft: (ReviewDraft) -> Unit = {},
    onPublishReview: (ReviewDraft) -> Unit = {}
) {
    val place = PreviewDataPopular.listPlaces.find {
        it.id == placeId
    }

    if (place == null) {
        return
    }

    val validInitialDraft = ReviewRules.normalizeDraft(
        draft = initialDraft?.takeIf { draft ->
            draft.placeId == place.id
        } ?: ReviewRules.emptyDraft(place.id),
        placeId = place.id
    )

    var rating by rememberSaveable(place.id) {
        mutableIntStateOf(validInitialDraft.rating)
    }
    var title by rememberSaveable(place.id) {
        mutableStateOf(validInitialDraft.title)
    }
    var description by rememberSaveable(place.id) {
        mutableStateOf(validInitialDraft.description)
    }
    var photoUris by rememberSaveable(place.id) {
        mutableStateOf(ArrayList(validInitialDraft.photoUris))
    }

    val isPublishEnabled by remember {
        derivedStateOf {
            ReviewRules.canPublish(
                rating = rating,
                title = title,
                description = description
            )
        }
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { selectedUri ->
        val selectedUriString = selectedUri?.toString()
        if (
            selectedUriString != null &&
            photoUris.size < ReviewRules.MAX_PHOTOS &&
            selectedUriString !in photoUris
        ) {
            photoUris = ArrayList(photoUris).apply {
                add(selectedUriString)
            }
        }
    }

    val reviewDraft = ReviewDraft(
        placeId = place.id,
        rating = rating,
        title = title,
        description = description,
        photoUris = photoUris.toList()
    )

    BackHandler(onBack = onBackClick)

    NewReviewContent(
        place = place,
        reviewDraft = reviewDraft,
        onRatingSelected = { selectedRating ->
            rating = selectedRating.coerceIn(
                ReviewRules.MIN_RATING,
                ReviewRules.MAX_RATING
            )
        },
        onTitleChange = { value ->
            title = value.take(ReviewRules.MAX_TITLE_LENGTH)
        },
        onDescriptionChange = { value ->
            description = value.take(ReviewRules.MAX_DESCRIPTION_LENGTH)
        },
        onAddPhotosClick = {
            photoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        },
        onRemovePhotoClick = { photoUri ->
            photoUris = ArrayList(photoUris.filterNot { it == photoUri })
        },
        isPublishEnabled = isPublishEnabled,
        onBackClick = onBackClick,
        onSaveDraftClick = {
            onSaveDraft(reviewDraft)
        },
        onPublishClick = {
            if (isPublishEnabled) {
                onPublishReview(
                    reviewDraft.copy(
                        title = reviewDraft.title.trim(),
                        description = reviewDraft.description.trim()
                    )
                )
            }
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 16.dp)
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
            isPublishEnabled = ReviewRules.canPublish(
                rating = reviewDraft.rating,
                title = reviewDraft.title,
                description = reviewDraft.description
            ),
            onBackClick = {},
            onSaveDraftClick = {},
            onPublishClick = {}
        )
    }
}
