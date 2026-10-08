package com.example.wayspot.ui.screens.reviewdetail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.wayspot.R
import com.example.wayspot.data.dto.UserDto
import com.example.wayspot.data.model.PlaceInfo
import com.example.wayspot.data.model.ReviewInfo

@Composable
fun ReviewDetailHeader(
    review: ReviewInfo,
    place: PlaceInfo?,
    author: UserDto?,
    canManage: Boolean,
    onAuthorClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(review.title, style = MaterialTheme.typography.headlineSmall)
        place?.let { Text(it.title, style = MaterialTheme.typography.titleMedium) }
        author?.let {
            TextButton(onClick = onAuthorClick) { Text(it.name) }
        }
        Text(stringResource(R.string.review_rating_value, review.rating))
        Text(review.description, style = MaterialTheme.typography.bodyLarge)
        if (canManage) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                TextButton(onClick = onEditClick) { Text(stringResource(R.string.review_edit)) }
                TextButton(onClick = onDeleteClick) { Text(stringResource(R.string.review_delete)) }
            }
        }
    }
}
