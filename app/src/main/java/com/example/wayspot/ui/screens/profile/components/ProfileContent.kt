package com.example.wayspot.ui.screens.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.example.wayspot.data.model.ReviewInfo
import com.example.wayspot.data.model.UserProfile
import androidx.compose.ui.res.stringResource
import com.example.wayspot.R


/** Organiza la cabecera del perfil, sus accesos y el historial de reseñas en una lista con claves estables. */
@Composable
fun ProfileContent(
    user: UserProfile,
    reviews: List<ReviewInfo>,
    onEditProfileClick: () -> Unit,
    onSavedPlacesClick: () -> Unit,
    onSignOutClick: () -> Unit,
    onReviewClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    errorMessage: String? = null
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.secondary
                            )
                        )
                    )
                    .statusBarsPadding()
                    .padding(
                        top = 24.dp,
                        bottom = 20.dp
                    )
            ) {
                ProfileHeader(
                    user = user,
                    modifier = Modifier.fillMaxWidth()
                )

                ProfileStats(
                    stats = user.stats.copy(reviews = reviews.size),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 12.dp,
                            vertical = 12.dp
                        )
                )
            }
        }

        item {
            ProfileEditButton(
                onClick = onEditProfileClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 12.dp
                    )
            )
        }

        item {
            ProfileTabs(
                selectedTabIndex = 0,
                onTabSelected = { index ->
                    if (index == 1) {
                        onSavedPlacesClick()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 4.dp,
                        bottom = 4.dp
                    )
            )
        }

        if (isLoading) {
            item(key = "reviews-loading") { CircularProgressIndicator() }
        }
        errorMessage?.let { message ->
            item(key = "reviews-error") {
                Text(message, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(16.dp))
            }
        }
        if (reviews.isEmpty() && !isLoading && errorMessage == null) {
            item(key = "reviews-empty") {
                Text(
                    text = stringResource(R.string.profile_reviews_empty),
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        items(
            items = reviews,
            key = {
                it.id
            }
        ) { review ->
            ProfileReviewItem(
                review = review,
                onClick = { onReviewClick(review.id) },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            ProfileSignOutButton(
                onClick = onSignOutClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 32.dp,
                        vertical = 16.dp
                    )
            )
        }
    }
}

