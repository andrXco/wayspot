package com.example.wayspot.ui.screens.newreview.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.example.wayspot.R
import com.example.wayspot.data.model.ReviewRules
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.theme.WayspotTheme

@Composable
fun ReviewFormSection(
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    titleMaxLength: Int = ReviewRules.MAX_TITLE_LENGTH,
    descriptionMaxLength: Int = ReviewRules.MAX_DESCRIPTION_LENGTH
) {
    val safeTitleMaxLength = titleMaxLength.coerceAtLeast(0)
    val safeDescriptionMaxLength = descriptionMaxLength.coerceAtLeast(0)

    Column(modifier = modifier) {
        ReviewFieldLabel(text = stringResource(R.string.new_review_title_label))
        Spacer(modifier = Modifier.height(8.dp))
        ReviewTextField(
            value = title,
            onValueChange = { value ->
                onTitleChange(value.take(safeTitleMaxLength))
            },
            placeholder = stringResource(R.string.new_review_title_placeholder),
            fieldContentDescription = stringResource(R.string.new_review_title_label),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        ReviewFieldLabel(text = stringResource(R.string.new_review_description_label))
        Spacer(modifier = Modifier.height(8.dp))
        ReviewTextField(
            value = description,
            onValueChange = { value ->
                onDescriptionChange(value.take(safeDescriptionMaxLength))
            },
            placeholder = stringResource(R.string.new_review_description_placeholder),
            fieldContentDescription = stringResource(R.string.new_review_description_label),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 128.dp),
            singleLine = false,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Default
            ),
            minLines = 4,
            maxLines = 6
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(
                R.string.new_review_character_counter,
                description.length.coerceAtMost(safeDescriptionMaxLength),
                safeDescriptionMaxLength
            ),
            modifier = Modifier.align(Alignment.End),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
private fun ReviewFieldLabel(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
private fun ReviewTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    fieldContentDescription: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean,
    keyboardOptions: KeyboardOptions,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.72f),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        modifier = modifier.semantics {
            contentDescription = fieldContentDescription
        },
        singleLine = singleLine,
        keyboardOptions = keyboardOptions,
        minLines = minLines,
        maxLines = maxLines,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
        textStyle = MaterialTheme.typography.bodyMedium,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.24f),
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.18f),
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.36f),
            cursorColor = MaterialTheme.colorScheme.primary
        )
    )
}

@WayspotMultiPreview
@Composable
private fun ReviewFormSectionPreview() {
    WayspotTheme {
        ReviewFormSection(
            title = "",
            onTitleChange = {},
            description = "",
            onDescriptionChange = {}
        )
    }
}
