package com.example.wayspot.ui.screens.placedetail.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import com.example.wayspot.R
import com.example.wayspot.data.model.place.PlaceInfo
import com.example.wayspot.ui.theme.WayspotTheme

/** Cuerpo del detalle con metadatos, descripción y reseñas. */
@Composable
fun PlaceDetailBody(
    place: PlaceInfo,
    onShareClick: () -> Unit,
    onWriteReviewClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(
            topStart = 28.dp,
            topEnd = 28.dp
        ),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = 20.dp,
                vertical = 24.dp
            )
        ) {
            PlaceMetadata(
                place = place,
                onShareClick = onShareClick
            )

            if (place.duration != null || place.price != null || place.altitude != null) {
                Spacer(modifier = Modifier.height(20.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    DetailValue(place.duration, Icons.Default.AccessTime, Modifier.weight(1f))
                    DetailValue(place.price, Icons.Default.ConfirmationNumber, Modifier.weight(1f))
                    DetailValue(place.altitude, Icons.Default.Place, Modifier.weight(1f))
                }
            }

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            PlaceDescription(
                description = place.description
            )

            if (place.reviewCount > 0) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(R.string.reviews_count, place.reviewCount.toString()),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp
                )
            }

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            WriteReviewButton(
                onClick = onWriteReviewClick
            )

        }
    }
}

@Composable
private fun DetailValue(value: String?, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier = Modifier) {
    if (value != null) {
        Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
        }
    }
}

@Composable
private fun PlaceDescription(
    description: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.description_title),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = description,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 22.sp
        )
    }
}

@Composable
private fun WriteReviewButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(14.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = null
        )

        Spacer(
            modifier = Modifier.width(8.dp)
        )

        Text(
            text = stringResource(R.string.button_write_review)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PlaceDetailBodyPreview() {
    WayspotTheme {
        PlaceDetailBody(
            place = PlaceInfo(
                id = "1",
                title = "Monserrate",
                category = "Turismo",
                location = "Bogotá, Colombia",
                rating = 4.7,
                imageUrl = null,
                description = "Lugar turístico ubicado en Bogotá"
            ),
            onShareClick = {},
            onWriteReviewClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PlaceDescriptionPreview() {
    WayspotTheme {
        PlaceDescription(
            description = "Lugar turístico ubicado en Bogotá"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun WriteReviewButtonPreview() {
    WayspotTheme {
        WriteReviewButton(
            onClick = {}
        )
    }
}
