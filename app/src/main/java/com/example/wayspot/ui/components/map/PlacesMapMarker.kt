package com.example.wayspot.ui.components.map

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.theme.WayspotTheme
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

internal object PlacesMapMarkerDimensions {
    val canvasWidth = 36.dp
    val canvasHeight = 42.dp
    val defaultWidth = 20.dp
    val defaultHeight = 28.dp
    val selectedWidth = 24.dp
    val selectedHeight = 32.dp
    val strokeWidth = 1.5.dp
}

/** Marcador de WaySpot con silueta direccional y selección por tamaño. */
@Composable
fun PlacesMapMarker(
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    val markerWidth = if (isSelected) {
        PlacesMapMarkerDimensions.selectedWidth
    } else {
        PlacesMapMarkerDimensions.defaultWidth
    }
    val markerHeight = if (isSelected) {
        PlacesMapMarkerDimensions.selectedHeight
    } else {
        PlacesMapMarkerDimensions.defaultHeight
    }

    Box(
        modifier = modifier.size(
            width = PlacesMapMarkerDimensions.canvasWidth,
            height = PlacesMapMarkerDimensions.canvasHeight
        ),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .size(width = markerWidth, height = markerHeight)
                .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen),
            shape = WaySpotMarkerShape,
            color = colorScheme.primary,
            contentColor = colorScheme.onPrimary,
            border = androidx.compose.foundation.BorderStroke(
                width = PlacesMapMarkerDimensions.strokeWidth,
                color = colorScheme.surface
            ),
            shadowElevation = if (isSelected) 4.dp else 2.dp
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val headCenter = size.width / 2f
                val innerRadius = size.width * 0.22f

                drawCircle(
                    color = colorScheme.onPrimary,
                    radius = innerRadius,
                    center = androidx.compose.ui.geometry.Offset(headCenter, headCenter),
                    blendMode = androidx.compose.ui.graphics.BlendMode.Clear
                )
            }
        }
    }
}

private object WaySpotMarkerShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val w = size.width
        val h = size.height

        val r = w / 2f
        val d = (h - r).coerceAtLeast(r + 1f)

        val sinAlpha = (r / d).coerceIn(0f, 0.999f)
        val cosAlpha = kotlin.math.sqrt(1f - sinAlpha * sinAlpha)

        val tLeftX = w / 2f - r * cosAlpha
        val tLeftY = r + r * sinAlpha

        val alphaDeg = Math.toDegrees(kotlin.math.asin(sinAlpha.toDouble())).toFloat()
        val startAngle = 90f + alphaDeg
        val sweepAngle = 360f - 2f * alphaDeg

        val tipRadius = w * 0.08f

        val path = Path().apply {
            addArc(
                oval = androidx.compose.ui.geometry.Rect(0f, 0f, w, w),
                startAngleDegrees = startAngle,
                sweepAngleDegrees = sweepAngle
            )
            lineTo(w / 2f + tipRadius, h - tipRadius * 1.5f)
            quadraticTo(
                w / 2f, h,
                w / 2f - tipRadius, h - tipRadius * 1.5f
            )
            lineTo(tLeftX, tLeftY)
            close()
        }
        return Outline.Generic(path)
    }
}

@WayspotMultiPreview
@Composable
private fun PlacesMapMarkerPreview() {
    WayspotTheme {
        Surface {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PlacesMapMarker(isSelected = false)
                PlacesMapMarker(isSelected = true)
            }
        }
    }
}
