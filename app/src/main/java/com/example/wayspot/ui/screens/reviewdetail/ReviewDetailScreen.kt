package com.example.wayspot.ui.screens.reviewdetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.wayspot.R
import com.example.wayspot.data.model.BackendSession
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.screens.reviewdetail.components.ReviewCommentForm
import com.example.wayspot.ui.screens.reviewdetail.components.ReviewCommentItem
import com.example.wayspot.ui.screens.reviewdetail.components.ReviewDetailHeader
import com.example.wayspot.ui.theme.WayspotTheme

@Composable
fun ReviewDetailScreen(
    viewModel: ReviewDetailViewModel,
    reviewId: String,
    onBackClick: () -> Unit,
    onAuthorClick: (String) -> Unit,
    onEditClick: (String) -> Unit,
    onDeleted: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    LaunchedEffect(reviewId) { viewModel.loadReview(reviewId) }
    LaunchedEffect(state.isDeleted) {
        if (state.isDeleted) onDeleted()
    }
    ReviewDetailContent(
        state = state,
        onBackClick = onBackClick,
        onAuthorClick = onAuthorClick,
        onEditClick = { onEditClick(reviewId) },
        onDeleteClick = viewModel::requestDelete,
        onConfirmDelete = viewModel::deleteReview,
        onDismissDelete = viewModel::dismissDelete,
        onCommentChange = viewModel::updateCommentDraft,
        onPublishComment = viewModel::publishComment,
        modifier = modifier
    )
}

@Composable
fun ReviewDetailContent(
    state: ReviewDetailState,
    onBackClick: () -> Unit,
    onAuthorClick: (String) -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onConfirmDelete: () -> Unit,
    onDismissDelete: () -> Unit,
    onCommentChange: (String) -> Unit,
    onPublishComment: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize().imePadding()) {
        TextButton(onClick = onBackClick) { Text(stringResource(R.string.volver)) }
        if (state.isLoading) CircularProgressIndicator()
        state.errorMessage?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        LazyColumn(modifier = Modifier.weight(1f)) {
            state.review?.let { review ->
                item(key = "review") {
                    ReviewDetailHeader(
                        review = review,
                        place = state.place,
                        author = state.author,
                        canManage = review.userId == BackendSession.USER_ID,
                        onAuthorClick = { onAuthorClick(review.userId) },
                        onEditClick = onEditClick,
                        onDeleteClick = onDeleteClick,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item(key = "comments-title") {
                    Text(stringResource(R.string.review_comments_title), style = MaterialTheme.typography.titleMedium)
                }
                if (state.comments.isEmpty()) {
                    item(key = "comments-empty") { Text(stringResource(R.string.review_comments_empty)) }
                } else {
                    items(state.comments, key = { it.id }) { comment ->
                        ReviewCommentItem(
                            comment = comment,
                            onAuthorClick = { onAuthorClick(comment.author.id) }
                        )
                    }
                }
            }
        }
        if (state.review != null) {
            ReviewCommentForm(
                draft = state.commentDraft,
                isPosting = state.isPosting,
                onDraftChange = onCommentChange,
                onPublishClick = onPublishComment
            )
        }
    }
    if (state.showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = onDismissDelete,
            title = { Text(stringResource(R.string.review_delete_confirm_title)) },
            text = { Text(stringResource(R.string.review_delete_confirm_body)) },
            confirmButton = {
                TextButton(onClick = onConfirmDelete) { Text(stringResource(R.string.review_delete)) }
            },
            dismissButton = {
                TextButton(onClick = onDismissDelete) { Text(stringResource(R.string.review_cancel)) }
            }
        )
    }
}

@WayspotMultiPreview
@Composable
private fun ReviewDetailScreenPreview() {
    WayspotTheme {
        ReviewDetailContent(
            state = ReviewDetailState(),
            onBackClick = {},
            onAuthorClick = {},
            onEditClick = {},
            onDeleteClick = {},
            onConfirmDelete = {},
            onDismissDelete = {},
            onCommentChange = {},
            onPublishComment = {}
        )
    }
}
