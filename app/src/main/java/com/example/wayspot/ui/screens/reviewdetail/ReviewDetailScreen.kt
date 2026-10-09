package com.example.wayspot.ui.screens.reviewdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.wayspot.R
import com.example.wayspot.data.dto.BackendSession
import com.example.wayspot.data.local.PreviewData
import com.example.wayspot.data.local.PreviewDataPopular
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.screens.reviewdetail.components.ReviewCommentAction
import com.example.wayspot.ui.screens.reviewdetail.components.ReviewCommentItem
import com.example.wayspot.ui.screens.reviewdetail.components.ReviewCommentsHeader
import com.example.wayspot.ui.screens.reviewdetail.components.ReviewDetailHeader
import com.example.wayspot.ui.screens.reviewdetail.components.ReviewDetailTopBar
import com.example.wayspot.ui.theme.WayspotTheme

@Composable
fun ReviewDetailScreen(
    viewModel: ReviewDetailViewModel,
    reviewId: String,
    onBackClick: () -> Unit,
    onCommentClick: (String) -> Unit,
    commentsRefreshReviewId: String?,
    commentsRefreshVersion: Int,
    onCommentsRefreshConsumed: (String, Int) -> Unit,
    onAuthorClick: (String) -> Unit,
    onEditClick: (String) -> Unit,
    onDeleted: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    LaunchedEffect(reviewId) { viewModel.loadReview(reviewId) }
    LaunchedEffect(reviewId, commentsRefreshReviewId, commentsRefreshVersion) {
        if (reviewId == commentsRefreshReviewId && commentsRefreshVersion > 0) {
            viewModel.loadReview(reviewId)
            onCommentsRefreshConsumed(reviewId, commentsRefreshVersion)
        }
    }
    LaunchedEffect(state.isDeleted) {
        if (state.isDeleted) onDeleted()
    }
    ReviewDetailContent(
        state = state,
        onBackClick = onBackClick,
        onCommentClick = { onCommentClick(reviewId) },
        onAuthorClick = onAuthorClick,
        onEditClick = { onEditClick(reviewId) },
        onDeleteClick = viewModel::requestDelete,
        onConfirmDelete = viewModel::deleteReview,
        onDismissDelete = viewModel::dismissDelete,
        modifier = modifier
    )
}

@Composable
fun ReviewDetailContent(
    state: ReviewDetailState,
    onBackClick: () -> Unit,
    onCommentClick: () -> Unit,
    onAuthorClick: (String) -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onConfirmDelete: () -> Unit,
    onDismissDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ReviewDetailTopBar(
            onBackClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        )
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 4.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (state.isLoading) {
                item(key = "loading") {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }

            state.errorMessage?.let { errorMessage ->
                item(key = "error") {
                    Text(
                        text = errorMessage,
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            state.review?.let { review ->
                item(key = "review") {
                    ReviewDetailHeader(
                        review = review,
                        place = state.place,
                        author = state.author,
                        canManage = review.userId == BackendSession.USER_ID,
                        isDeleting = state.isDeleting,
                        onAuthorClick = { onAuthorClick(review.userId) },
                        onEditClick = onEditClick,
                        onDeleteClick = onDeleteClick,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item(key = "comments-header") {
                    ReviewCommentsHeader(
                        count = state.comments.size,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                if (state.comments.isEmpty()) {
                    item(key = "comments-empty") {
                        Text(
                            text = stringResource(R.string.review_comments_empty),
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                } else {
                    items(state.comments, key = { it.id }) { comment ->
                        ReviewCommentItem(
                            comment = comment,
                            onAuthorClick = { onAuthorClick(comment.author.id.toString()) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                item(key = "comments-action") {
                    ReviewCommentAction(
                        onClick = onCommentClick,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }

    if (state.showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = onDismissDelete,
            title = { Text(stringResource(R.string.review_delete_confirm_title)) },
            text = { Text(stringResource(R.string.review_delete_confirm_body)) },
            confirmButton = {
                TextButton(
                    onClick = onConfirmDelete,
                    enabled = !state.isDeleting
                ) {
                    Text(stringResource(R.string.review_delete))
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissDelete) {
                    Text(stringResource(R.string.review_cancel))
                }
            }
        )
    }
}

@WayspotMultiPreview
@Composable
private fun ReviewDetailScreenPreview() {
    WayspotTheme {
        ReviewDetailContent(
            state = ReviewDetailState(
                review = PreviewData.reviewDetailReview,
                place = PreviewDataPopular.previewPlaceInfo,
                author = PreviewData.reviewDetailAuthor,
                comments = PreviewData.reviewDetailComments
            ),
            onBackClick = {},
            onCommentClick = {},
            onAuthorClick = {},
            onEditClick = {},
            onDeleteClick = {},
            onConfirmDelete = {},
            onDismissDelete = {}
        )
    }
}
