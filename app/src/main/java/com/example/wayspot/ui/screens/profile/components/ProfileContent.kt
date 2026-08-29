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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.example.wayspot.data.model.Review
import com.example.wayspot.data.model.UserProfile

@Composable
fun ProfileContent(
    user: UserProfile,
    reviews: List<Review>,
    onEditProfileClick: () -> Unit,
    onSavedPlacesClick: () -> Unit,
    modifier: Modifier = Modifier
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
                    stats = user.stats,
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

        items(
            items = reviews,
            key = {
                it.placeTitle + it.fecha + it.usuario
            }
        ) { review ->
            ProfileReviewItem(
                review = review,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
