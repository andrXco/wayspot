package com.example.wayspot.ui.screens.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wayspot.R
import com.example.wayspot.data.model.Review
import com.example.wayspot.data.model.UserProfile
import com.example.wayspot.ui.screens.profile.components.ProfileEditButton
import com.example.wayspot.ui.screens.profile.components.ProfileHeader
import com.example.wayspot.ui.screens.profile.components.ProfileReviewItem
import com.example.wayspot.ui.screens.profile.components.ProfileStats
import com.example.wayspot.ui.screens.profile.components.ProfileTabs

@Composable
fun ProfileContent(
    user: UserProfile,
    reviews: List<Review>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    onEditProfileClick: () -> Unit,
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
                selectedTabIndex = selectedTabIndex,
                onTabSelected = onTabSelected,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 4.dp,
                        bottom = 4.dp
                    )
            )
        }

        if (selectedTabIndex == 0) {
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
        } else {
            item {
                ProfileEmptySavedSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 24.dp,
                            vertical = 40.dp
                        )
                )
            }
        }
    }
}

@Composable
private fun ProfileEmptySavedSection(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.BookmarkBorder,
                contentDescription = stringResource(
                    R.string.profile_saved_empty_icon_content_description
                ),
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(
            text = stringResource(
                R.string.profile_saved_empty_title
            ),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(
            modifier = Modifier.height(6.dp)
        )

        Text(
            text = stringResource(
                R.string.profile_saved_empty_description
            ),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}
