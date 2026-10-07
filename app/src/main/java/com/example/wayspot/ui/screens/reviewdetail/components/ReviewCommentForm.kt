package com.example.wayspot.ui.screens.reviewdetail.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.wayspot.R

@Composable
fun ReviewCommentForm(
    draft: String,
    isPosting: Boolean,
    onDraftChange: (String) -> Unit,
    onPublishClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(16.dp)) {
        OutlinedTextField(
            value = draft,
            onValueChange = onDraftChange,
            label = { Text(stringResource(R.string.review_comment_hint)) },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onPublishClick,
            enabled = draft.isNotBlank() && !isPosting,
            modifier = Modifier.padding(top = 8.dp)
        ) { Text(stringResource(R.string.review_comment_publish)) }
    }
}
