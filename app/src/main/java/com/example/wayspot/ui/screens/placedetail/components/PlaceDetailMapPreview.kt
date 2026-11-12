package com.example.wayspot.ui.screens.placedetail.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wayspot.R
import com.example.wayspot.data.local.PreviewDataPopular
import com.example.wayspot.data.model.place.toPlaceInfo
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.theme.WayspotTheme

/** Vista ilustrada del mapa del lugar hasta que el detalle tenga coordenadas y Maps SDK. */
@Composable
fun PlaceDetailMapPreview(
    placeName: String,
    location: String,
    onMapClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colorScheme = MaterialTheme.colorScheme
    // Access string resource place_map_preview_content_description with placeName and location parameters
    val mapDescription = stringResource(
        R.string.place_map_preview_content_description,
        placeName,
        location,
    )

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Column {
                Text(
                    text = stringResource(R.string.place_map_section_label),
                    color = colorScheme.onSurfaceVariant,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )
                Text(
                    text = stringResource(R.string.place_map_explore_title),
                    color = colorScheme.onSurface,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            TextButton(
                onClick = onMapClick,
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp)
            ) {
                Text(
                    text = stringResource(R.string.place_map_view_action),
                    color = colorScheme.primary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(154.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(colorScheme.surfaceVariant)
                .border(
                    width = 1.dp,
                    color = colorScheme.outline.copy(alpha = 0.28f),
                    shape = RoundedCornerShape(18.dp)
                )
                .semantics { contentDescription = mapDescription }
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val width = size.width
                val height = size.height
                val broadRoad = Path().apply {
                    moveTo(-width * 0.06f, height * 0.2f)
                    cubicTo(width * 0.18f, height * 0.02f, width * 0.24f, height * 0.42f, width * 0.48f, height * 0.24f)
                    cubicTo(width * 0.69f, height * 0.08f, width * 0.8f, height * 0.38f, width * 1.08f, height * 0.12f)
                }
                val lowerRoad = Path().apply {
                    moveTo(-width * 0.05f, height * 0.88f)
                    cubicTo(width * 0.2f, height * 0.65f, width * 0.28f, height * 0.76f, width * 0.46f, height * 0.57f)
                    cubicTo(width * 0.66f, height * 0.38f, width * 0.77f, height * 0.86f, width * 1.06f, height * 0.64f)
                }
                val crossRoad = Path().apply {
                    moveTo(width * 0.12f, -height * 0.08f)
                    cubicTo(width * 0.3f, height * 0.22f, width * 0.48f, height * 0.45f, width * 0.72f, height * 1.08f)
                }
                val upperRoad = Path().apply {
                    moveTo(width * 0.58f, -height * 0.08f)
                    cubicTo(width * 0.53f, height * 0.28f, width * 0.84f, height * 0.32f, width * 1.06f, height * 0.48f)
                }
                val roadWidth = 22.dp.toPx()
                val roadCenterWidth = 4.dp.toPx()

                listOf(broadRoad, lowerRoad, crossRoad, upperRoad).forEach { road ->
                    drawPath(
                        path = road,
                        color = colorScheme.surface.copy(alpha = 0.82f),
                        style = Stroke(width = roadWidth, cap = StrokeCap.Round)
                    )
                    drawPath(
                        path = road,
                        color = colorScheme.outlineVariant.copy(alpha = 0.65f),
                        style = Stroke(width = roadCenterWidth, cap = StrokeCap.Round)
                    )
                }

                val greenSpace = Path().apply {
                    moveTo(width * 0.72f, height * 0.02f)
                    cubicTo(width * 0.79f, height * 0.17f, width * 0.86f, height * 0.23f, width * 1.02f, height * 0.18f)
                }
                drawPath(
                    path = greenSpace,
                    color = colorScheme.secondary.copy(alpha = 0.45f),
                    style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
                )
            }

            Icon(
                imageVector = Icons.Default.Place,
                contentDescription = null,
                tint = colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(40.dp)
            )

            Surface(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(10.dp)
                    .size(34.dp),
                shape = CircleShape,
                color = colorScheme.surface.copy(alpha = 0.96f),
                shadowElevation = 2.dp
            ) {
                IconButton(onClick = onMapClick) {
                    Icon(
                        imageVector = Icons.Default.MyLocation,
                        contentDescription = stringResource(
                            R.string.place_map_center_content_description,
                            placeName
                        ),
                        tint = colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Surface(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(10.dp),
                shape = RoundedCornerShape(10.dp),
                color = colorScheme.surface.copy(alpha = 0.96f),
                shadowElevation = 2.dp
            ) {
                Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp)) {
                    Text(
                        text = placeName,
                        color = colorScheme.onSurface,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = location,
                        color = colorScheme.onSurfaceVariant,
                        fontSize = 9.sp
                    )
                }
            }
        }
    }
}

@WayspotMultiPreview
@Composable
private fun PlaceDetailMapPreviewComponentPreview() {
    val context = LocalContext.current
    val place = PreviewDataPopular.cerroMonserrate.toPlaceInfo(context::getString)

    WayspotTheme {
        PlaceDetailMapPreview(
            placeName = place.title,
            location = place.location,
            onMapClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
