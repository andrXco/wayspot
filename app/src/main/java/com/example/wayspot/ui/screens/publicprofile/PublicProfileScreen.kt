package com.example.wayspot.ui.screens.publicprofile

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.screens.profile.components.ProfileInfo
import com.example.wayspot.ui.screens.profile.components.ProfileReviewItem
import com.example.wayspot.ui.theme.WayspotTheme

@Composable
fun PublicProfileScreen(
    viewModel: PublicProfileViewModel,
    userId: String,
    onBackClick: () -> Unit,
    onReviewClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    LaunchedEffect(userId) { viewModel.loadUser(userId) }
    PublicProfileContent(state, onBackClick, onReviewClick, modifier)
}

@Composable
fun PublicProfileContent(
    state: PublicProfileState,
    onBackClick: () -> Unit,
    onReviewClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        item(key = "back") { TextButton(onClick = onBackClick) { Text(stringResource(R.string.volver)) } }
        if (state.isLoading) item(key = "loading") { CircularProgressIndicator() }
        state.errorMessage?.let { message ->
            item(key = "error") { Text(message, color = MaterialTheme.colorScheme.error) }
        }
        state.user?.let { user ->
            item(key = "profile") {
                ProfileInfo(
                    name = user.name,
                    username = stringResource(R.string.profile_username_format, user.username),
                    description = user.bio.orEmpty(),
                    initials = user.name.take(1).uppercase()
                )
            }
            if (state.reviews.isEmpty()) {
                item(key = "empty") { Text(stringResource(R.string.profile_reviews_empty)) }
            } else {
                items(state.reviews, key = { it.id }) { review ->
                    ProfileReviewItem(review = review, onClick = { onReviewClick(review.id) })
                }
            }
        }
    }
}

@WayspotMultiPreview
@Composable
private fun PublicProfileScreenPreview() {
    WayspotTheme {
        PublicProfileContent(PublicProfileState(), onBackClick = {}, onReviewClick = {})
    }
}
