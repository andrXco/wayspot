package com.example.wayspot.ui.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.example.wayspot.R
import com.example.wayspot.data.Post
import com.example.wayspot.data.local.PreviewData
import com.example.wayspot.ui.theme.EstrellaAmarilla
import com.example.wayspot.ui.theme.WayspotTheme

@Composable
fun PostCard(
    post: Post,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Imagen de Fondo
            AsyncImage(
                model = post.imagen ?: R.drawable.post_card_machu_pichu, // Imagen por defecto
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.branding_logo_claro_wayspot),
                error = painterResource(R.drawable.branding_logo_claro_wayspot)
            )

            // Gradiente para asegurar legibilidad del texto
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            0f to Color.Transparent,
                            0.3f to Color.Black.copy(alpha = 0.1f),
                            0.5f to Color.Black.copy(alpha = 0.5f),
                            1.5f to Color.Black.copy(alpha = 0.8f),
                            2.5f to Color.Black.copy(alpha = 0.95f)
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = post.nombre,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            shadow = Shadow(
                                color = Color.Black,
                                offset = Offset(2f, 2f),
                                blurRadius = 4f
                            )
                        )
                    )
                    Text(
                        text = post.tiempo, 
                        color = Color.White, 
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        style = MaterialTheme.typography.bodySmall.copy(
                            shadow = Shadow(color = Color.Black, blurRadius = 8f)
                        )
                    )
                }
                
                Text(
                    text = post.usuario, 
                    color = Color.White.copy(alpha = 0.7f), 
                    fontSize = 12.sp,
                    style = MaterialTheme.typography.bodySmall.copy(
                        shadow = Shadow(color = Color.Black, blurRadius = 2f)
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = post.categoria,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelMedium.copy(
                            shadow = Shadow(color = Color.Black, blurRadius = 4f)
                        )
                    )
                    Row {
                        repeat(post.rating) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = stringResource(R.string.star_content_description),
                                tint = EstrellaAmarilla,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = post.titulo,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        shadow = Shadow(
                            color = Color.Black,
                            offset = Offset(2f, 2f),
                            blurRadius = 6f
                        )
                    )
                )
                Text(
                    text = post.ubicacion, 
                    color = Color.White.copy(alpha = 0.9f), 
                    fontSize = 14.sp,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        shadow = Shadow(color = Color.Black, blurRadius = 4f)
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PostCardPreview() {
    WayspotTheme {
        PostCard(post = PreviewData.samplePost1, onClick = {})
    }
}
