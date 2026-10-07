package com.example.wayspot.ui.screens.reviewdetail.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.wayspot.data.model.ReviewComment

@Composable
fun ReviewCommentItem(
    comment: ReviewComment,
    onAuthorClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        TextButton(onClick = onAuthorClick) { Text(comment.author.name) }
        Text(comment.content, style = MaterialTheme.typography.bodyMedium)
    }
}
