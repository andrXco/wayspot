package com.example.wayspot.ui.screens.profile.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wayspot.R
import com.example.wayspot.model.UserReview
import com.example.wayspot.ui.theme.EstrellaAmarilla
import com.example.wayspot.ui.theme.VerdeBosque

@Composable
fun ReviewItem(
    review: UserReview,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = review.image as? Int ?: R.drawable.post_card_machu_pichu),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = review.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = review.date,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = VerdeBosque,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = review.location,
                        style = MaterialTheme.typography.bodySmall,
                        color = VerdeBosque
                    )
                }

                Row(
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    repeat(5) { index ->
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = if (index < review.rating) EstrellaAmarilla else Color.LightGray,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Text(
                    text = review.comment,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.DarkGray
                )
            }
        }
    }
}
