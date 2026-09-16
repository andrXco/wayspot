package com.example.wayspot.ui.screens.profile.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.wayspot.R
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.theme.WayspotTheme

@Composable
fun ProfileSignOutButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.error
        )
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Outlined.Logout,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = stringResource(R.string.profile_button_sign_out),
            color = MaterialTheme.colorScheme.error,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@WayspotMultiPreview
@Composable
private fun ProfileSignOutButtonPreview() {
    WayspotTheme {
        ProfileSignOutButton(onClick = {})
    }
}
