package com.example.wayspot.ui.screens.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.wayspot.R
import com.example.wayspot.data.model.ReviewFeedItem

@Composable
fun RemoteReviewCard(
    item: ReviewFeedItem,
    onReviewClick: () -> Unit,
    onAuthorClick: () -> Unit,
    onPlaceClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextButton(onClick = onAuthorClick) {
                Text(item.author.name, fontWeight = FontWeight.Bold)
                Text(
                    text = stringResource(R.string.profile_username_format, item.author.username),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Text(
                text = item.review.title,
                modifier = Modifier.clickable(onClick = onReviewClick),
                style = MaterialTheme.typography.titleMedium
            )
            Text(stringResource(R.string.review_rating_value, item.review.rating))
            Text(
                text = item.review.description,
                modifier = Modifier.clickable(onClick = onReviewClick),
                style = MaterialTheme.typography.bodyMedium
            )
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                TextButton(onClick = onPlaceClick) { Text(item.place.title) }
                TextButton(onClick = onReviewClick) {
                    Text(stringResource(R.string.review_comments_count, item.review.commentCount))
                }
            }
        }
    }
}
