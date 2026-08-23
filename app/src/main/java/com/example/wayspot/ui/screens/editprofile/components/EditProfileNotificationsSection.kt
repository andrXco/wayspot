package com.example.wayspot.ui.screens.editprofile.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.wayspot.R

@Composable
fun EditProfileNotificationsSection(
    newFollowersEnabled: Boolean,
    onNewFollowersChange: (Boolean) -> Unit,
    reviewCommentsEnabled: Boolean,
    onReviewCommentsChange: (Boolean) -> Unit,
    receivedLikesEnabled: Boolean,
    onReceivedLikesChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.18f),
        contentColor = MaterialTheme.colorScheme.onSurface,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.28f)
        )
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Text(
                text = stringResource(R.string.edit_profile_notifications_title),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )

            HorizontalDivider(
                modifier = Modifier.padding(top = 10.dp),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.24f)
            )

            NotificationPreferenceRow(
                label = stringResource(R.string.edit_profile_new_followers),
                isChecked = newFollowersEnabled,
                onCheckedChange = onNewFollowersChange,
                switchContentDescription = stringResource(
                    R.string.edit_profile_new_followers_switch_content_description
                )
            )
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.20f)
            )
            NotificationPreferenceRow(
                label = stringResource(R.string.edit_profile_review_comments),
                isChecked = reviewCommentsEnabled,
                onCheckedChange = onReviewCommentsChange,
                switchContentDescription = stringResource(
                    R.string.edit_profile_review_comments_switch_content_description
                )
            )
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.20f)
            )
            NotificationPreferenceRow(
                label = stringResource(R.string.edit_profile_likes_received),
                isChecked = receivedLikesEnabled,
                onCheckedChange = onReceivedLikesChange,
                switchContentDescription = stringResource(
                    R.string.edit_profile_likes_received_switch_content_description
                )
            )
        }
    }
}

@Composable
private fun NotificationPreferenceRow(
    label: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    switchContentDescription: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium
        )

        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.semantics {
                contentDescription = switchContentDescription
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                checkedTrackColor = MaterialTheme.colorScheme.primary,
                checkedBorderColor = MaterialTheme.colorScheme.primary,
                uncheckedThumbColor = MaterialTheme.colorScheme.surface,
                uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant,
                uncheckedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.36f)
            )
        )
    }
}
