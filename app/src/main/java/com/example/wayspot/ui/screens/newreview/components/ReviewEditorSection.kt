package com.example.wayspot.ui.screens.newreview.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.wayspot.data.model.Place
import com.example.wayspot.data.model.ReviewDraft

@Composable
fun ReviewEditorSection(
    place: Place,
    reviewDraft: ReviewDraft,
    onRatingSelected: (Int) -> Unit,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onAddPhotosClick: () -> Unit,
    onRemovePhotoClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        ReviewPlaceSummaryCard(
            place = place,
            modifier = Modifier.fillMaxWidth()
        )
        ReviewRatingSection(
            rating = reviewDraft.rating,
            onRatingSelected = onRatingSelected,
            modifier = Modifier.fillMaxWidth()
        )
        ReviewFormSection(
            title = reviewDraft.title,
            onTitleChange = onTitleChange,
            description = reviewDraft.description,
            onDescriptionChange = onDescriptionChange,
            modifier = Modifier.fillMaxWidth()
        )
        ReviewPhotoSection(
            photoUris = reviewDraft.photoUris,
            onAddPhotosClick = onAddPhotosClick,
            onRemovePhotoClick = onRemovePhotoClick,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}
