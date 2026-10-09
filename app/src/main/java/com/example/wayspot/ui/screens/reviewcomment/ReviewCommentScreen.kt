package com.example.wayspot.ui.screens.reviewcomment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.material3.MaterialTheme
import com.example.wayspot.R
import com.example.wayspot.data.local.PreviewData
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.screens.reviewcomment.components.ReviewCommentEditor
import com.example.wayspot.ui.screens.reviewcomment.components.ReviewCommentHeader
import com.example.wayspot.ui.theme.WayspotTheme

@Composable
fun ReviewCommentScreen(
    viewModel: ReviewCommentViewModel,
    reviewId: String,
    onBackClick: () -> Unit,
    onPublished: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(reviewId) {
        viewModel.setReviewId(reviewId)
    }
    LaunchedEffect(state.isPublished) {
        if (state.isPublished) {
            onPublished(reviewId)
            viewModel.consumePublished()
        }
    }

    ReviewCommentContent(
        state = state,
        onBackClick = onBackClick,
        onDraftChange = viewModel::updateDraft,
        onPublishClick = viewModel::publishComment,
        modifier = modifier
    )
}

@Composable
fun ReviewCommentContent(
    state: ReviewCommentState,
    onBackClick: () -> Unit,
    onDraftChange: (String) -> Unit,
    onPublishClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .imePadding()
    ) {
        ReviewCommentHeader(onBackClick = onBackClick)
        ReviewCommentEditor(
            draft = state.draft,
            isPublishing = state.isPublishing,
            canPublish = state.canPublish,
            errorMessage = state.errorMessage?.ifBlank {
                stringResource(R.string.review_comment_publish_error)
            },
            onDraftChange = onDraftChange,
            onPublishClick = onPublishClick,
            modifier = Modifier.weight(1f)
        )
    }
}

@WayspotMultiPreview
@Composable
private fun ReviewCommentScreenPreview() {
    WayspotTheme {
        ReviewCommentContent(
            state = ReviewCommentState(
                reviewId = PreviewData.reviewCommentPreviewReviewId,
                draft = PreviewData.reviewCommentDraft
            ),
            onBackClick = {},
            onDraftChange = {},
            onPublishClick = {}
        )
    }
}
