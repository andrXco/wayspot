package com.example.wayspot.ui.screens.placedetail.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wayspot.data.model.Places
import com.example.wayspot.data.local.PreviewDataPopular
import com.example.wayspot.ui.theme.WayspotTheme

@Composable
internal fun PlaceInfoRow(place: Places, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        PlaceInfoCard(
            icon = Icons.Default.AccessTime,
            textRes = place.detail.durationRes,
            modifier = Modifier.weight(1f)
        )
        PlaceInfoCard(
            icon = Icons.Default.ConfirmationNumber,
            textRes = place.detail.priceRes,
            modifier = Modifier.weight(1f)
        )
        PlaceInfoCard(
            icon = Icons.Default.Place,
            textRes = place.detail.altitudeRes,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun PlaceInfoCard(
    icon: ImageVector,
    textRes: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(80.dp),
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(textRes),
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PlaceInfoRowPreview() {
    WayspotTheme {
        PlaceInfoRow(place = PreviewDataPopular.samplePlaces1)
    }
}

@Preview(showBackground = true)
@Composable
private fun PlaceInfoCardPreview() {
    WayspotTheme {
        PlaceInfoCard(
            icon = Icons.Default.AccessTime,
            textRes = PreviewDataPopular.samplePlaces1.detail.durationRes
        )
    }
}
