package com.example.wayspot.ui.screens.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.wayspot.model.Review
import com.example.wayspot.ui.preview.PreviewData
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.screens.profile.components.ProfileHeader
import com.example.wayspot.ui.screens.profile.components.ProfileInfo
import com.example.wayspot.ui.screens.profile.components.ProfileReviewItem
import com.example.wayspot.ui.screens.profile.components.ProfileStats
import com.example.wayspot.ui.theme.WayspotTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onEditProfileClick: () -> Unit = {}
) {
    ProfileContent(
        reviews = PreviewData.listReviews,
        onEditProfileClick = onEditProfileClick,
        modifier = modifier
    )
}

@Composable
fun ProfileContent(
    reviews: List<Review>,
    onEditProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        item {
            ProfileHeader()
        }

        item {
            ProfileInfo(
                name = "Valentina García",
                username = "@valentina_viaja",
                description = "Viajera apasionada · Compartiendo el mundo un lugar a la vez"
            )
        }

        item {
            ProfileStats(
                places = 47,
                reviews = 38,
                followers = "1.2k"
            )
        }

        item {
            ProfileActions(
                onEditProfileClick = onEditProfileClick
            )
        }

        items(reviews) { review ->
            ProfileReviewItem(
                review = review
            )
        }
    }
}

@Composable
private fun ProfileActions(
    onEditProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(
            horizontal = 16.dp,
            vertical = 12.dp
        )
    ) {
        androidx.compose.material3.OutlinedButton(
            onClick = onEditProfileClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            androidx.compose.material3.Text(
                text = "Editar perfil"
            )
        }
    }
}

@WayspotMultiPreview
@Composable
private fun ProfileScreenPreview() {
    WayspotTheme {
        ProfileScreen()
    }
}