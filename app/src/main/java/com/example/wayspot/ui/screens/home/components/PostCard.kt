package com.example.wayspot.ui.screens.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wayspot.model.Post
import com.example.wayspot.ui.preview.PreviewData
import com.example.wayspot.ui.theme.WayspotTheme

@Composable
fun PostCard(
    post: Post,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = post.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = post.usuario, color = Color.Gray, fontSize = 12.sp)
                Text(text = post.tiempo, color = Color.Gray, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = post.categoria,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Row {
                    repeat(post.rating) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Estrella",
                            tint = Color(0xFFFFC107),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = post.titulo, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(text = post.ubicacion, color = Color.Gray, fontSize = 14.sp)

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = post.descripcion,
                fontSize = 14.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PostCardPreview() {
    WayspotTheme {
        PostCard(post = PreviewData.samplePost1)
    }
}
