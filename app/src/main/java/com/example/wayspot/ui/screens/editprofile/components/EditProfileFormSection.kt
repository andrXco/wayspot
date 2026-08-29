package com.example.wayspot.ui.screens.editprofile.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.wayspot.R
import com.example.wayspot.data.model.EditProfileRules

@Composable
fun EditProfileFormSection(
    username: String,
    onUsernameChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    bio: String,
    onBioChange: (String) -> Unit,
    location: String,
    onLocationChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    bioMaxLength: Int = EditProfileRules.MAX_BIO_LENGTH
) {
    val safeBioMaxLength = bioMaxLength.coerceAtLeast(0)

    Column(modifier = modifier.fillMaxWidth()) {
        EditProfileFieldLabel(
            text = stringResource(R.string.edit_profile_username_label)
        )
        Spacer(modifier = Modifier.height(8.dp))
        EditProfileTextField(
            value = username,
            onValueChange = onUsernameChange,
            leadingIcon = Icons.Outlined.Person,
            leadingIconContentDescription = stringResource(
                R.string.edit_profile_username_icon_content_description
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Ascii,
                imeAction = ImeAction.Next
            )
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = stringResource(R.string.edit_profile_username_support),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelSmall
        )

        Spacer(modifier = Modifier.height(18.dp))

        EditProfileFieldLabel(
            text = stringResource(R.string.edit_profile_email_label)
        )
        Spacer(modifier = Modifier.height(8.dp))
        EditProfileTextField(
            value = email,
            onValueChange = onEmailChange,
            leadingIcon = Icons.Outlined.Email,
            leadingIconContentDescription = stringResource(
                R.string.edit_profile_email_icon_content_description
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(18.dp))

        EditProfileFieldLabel(
            text = stringResource(R.string.edit_profile_bio_label)
        )
        Spacer(modifier = Modifier.height(8.dp))
        EditProfileTextField(
            value = bio,
            onValueChange = { value ->
                onBioChange(value.take(safeBioMaxLength))
            },
            modifier = Modifier.heightIn(min = 96.dp),
            singleLine = false,
            minLines = 3,
            maxLines = 5,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Default
            )
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = stringResource(
                R.string.edit_profile_character_count,
                bio.length.coerceAtMost(safeBioMaxLength),
                safeBioMaxLength
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelSmall
        )

        Spacer(modifier = Modifier.height(18.dp))

        EditProfileFieldLabel(
            text = stringResource(R.string.edit_profile_location_label)
        )
        Spacer(modifier = Modifier.height(8.dp))
        EditProfileTextField(
            value = location,
            onValueChange = onLocationChange,
            leadingIcon = Icons.Outlined.LocationOn,
            leadingIconContentDescription = stringResource(
                R.string.edit_profile_location_icon_content_description
            ),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Done
            )
        )
    }
}

@Composable
private fun EditProfileFieldLabel(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun EditProfileTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    leadingIconContentDescription: String? = null,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        leadingIcon = leadingIcon?.let { icon ->
            {
                Icon(
                    imageVector = icon,
                    contentDescription = leadingIconContentDescription,
                    modifier = Modifier.height(20.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        singleLine = singleLine,
        minLines = minLines,
        maxLines = maxLines,
        keyboardOptions = keyboardOptions,
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
