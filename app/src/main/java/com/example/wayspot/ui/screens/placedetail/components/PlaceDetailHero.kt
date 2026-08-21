package com.example.wayspot.ui.screens.placedetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.wayspot.R
import com.example.wayspot.data.model.Places
import com.example.wayspot.data.local.PreviewDataPopular
import com.example.wayspot.ui.theme.Blanco
import com.example.wayspot.ui.theme.Carbon
import com.example.wayspot.ui.theme.WayspotTheme

@Composable
fun PlaceDetailHero(
    place: Places,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.height(336.dp)) {
        AsyncImage(
            model = place.imagen ?: R.drawable.post_card_machu_pichu,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.branding_logo_claro_wayspot)
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Blanco.copy(alpha = 0f), Carbon.copy(alpha = 0.68f))
                    )
                )
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DetailActionButton(
                onClick = onBackClick,
                icon = Icons.Default.ChevronLeft,
                contentDescription = stringResource(R.string.back_button_content_description)
            )
            DetailActionButton(
                onClick = onSaveClick,
                icon = Icons.Default.BookmarkBorder,
                contentDescription = stringResource(R.string.bookmark_content_description)
            )
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(horizontal = 24.dp, vertical = 28.dp)
        ) {
            Text(
                text = stringResource(
                    R.string.place_category_and_country,
                    stringResource(place.categoriaRes),
                    stringResource(place.ubicacionRes)
                ),
                color = Blanco,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(place.tituloRes),
                color = Blanco,
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlaceDetailHeroPreview() {
    WayspotTheme {
        PlaceDetailHero(
            place = PreviewDataPopular.samplePlaces1,
            onBackClick = {},
            onSaveClick = {}
        )
    }
}

@Composable
private fun DetailActionButton(
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String,
    m: Modifier = Modifier
) {
    Surface(
        modifier = m.size(40.dp),
        shape = RoundedCornerShape(12.dp),
        color = Blanco.copy(alpha = 0.9f)
    ) {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
