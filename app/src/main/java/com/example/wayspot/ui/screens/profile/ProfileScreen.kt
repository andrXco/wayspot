package com.example.wayspot.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wayspot.model.UserProfile
import com.example.wayspot.model.UserReview
import com.example.wayspot.ui.preview.ProfileData
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.screens.profile.components.ReviewItem
import com.example.wayspot.ui.theme.ArenaDorado
import com.example.wayspot.ui.theme.BlancoCalido
import com.example.wayspot.ui.theme.VerdeBosque
import com.example.wayspot.ui.theme.WayspotTheme

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier
) {
    ProfileContent(
        profile = ProfileData.userProfile,
        reviews = ProfileData.userReviews,
        modifier = modifier
    )
}

@Composable
fun ProfileContent(
    profile: UserProfile,
    reviews: List<UserReview>,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Mis reseñas", "Guardados")

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BlancoCalido)
    ) {
        item {
            ProfileHeader(profile = profile)
        }

        item {
            EditProfileButton()
        }

        item {
            ProfileTabs(
                tabs = tabs,
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }

        if (selectedTab == 0) {
            items(reviews) { review ->
                ReviewItem(
                    review = review,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
        
        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
private fun ProfileHeader(
    profile: UserProfile,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(VerdeBosque)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Top action icons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Surface(
                    onClick = {},
                    color = Color.White.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "(o)",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Profile Image / Initials
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f))
                        .border(2.dp, Color.White.copy(alpha = 0.5f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "VG",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                    
                    // Verification Badge
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .align(Alignment.BottomEnd)
                            .background(ArenaDorado, CircleShape)
                            .border(2.dp, VerdeBosque, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = profile.name,
                        color = Color.White,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = profile.username,
                        color = Color.White.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = profile.bio,
                color = Color.White,
                style = MaterialTheme.typography.bodySmall,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Stats Card
            Surface(
                color = Color.White.copy(alpha = 0.15f),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatItem(
                        value = profile.placesCount.toString(),
                        label = "Lugares",
                        modifier = Modifier.weight(1f)
                    )
                    VerticalDivider()
                    StatItem(
                        value = profile.reviewsCount.toString(),
                        label = "Reseñas",
                        modifier = Modifier.weight(1f)
                    )
                    VerticalDivider()
                    StatItem(
                        value = profile.followersCount,
                        label = "Seguidores",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun StatItem(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.8f),
            fontSize = 12.sp
        )
    }
}

@Composable
private fun VerticalDivider() {
    Box(
        modifier = Modifier
            .height(30.dp)
            .width(1.dp)
            .background(Color.White.copy(alpha = 0.3f))
    )
}

@Composable
private fun EditProfileButton() {
    OutlinedButton(
        onClick = {},
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        border = borderStroke(1.dp, VerdeBosque)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = VerdeBosque
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Editar perfil",
                color = VerdeBosque,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun ProfileTabs(
    tabs: List<String>,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    Column {
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.Transparent,
            contentColor = VerdeBosque,
            divider = {},
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = VerdeBosque
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { onTabSelected(index) },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == index) VerdeBosque else Color.Gray
                        )
                    }
                )
            }
        }
        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun borderStroke(width: androidx.compose.ui.unit.Dp, color: Color) = androidx.compose.foundation.BorderStroke(width, color)

@WayspotMultiPreview
@Composable
private fun ProfileScreenPreview() {
    WayspotTheme {
        ProfileScreen()
    }
}
