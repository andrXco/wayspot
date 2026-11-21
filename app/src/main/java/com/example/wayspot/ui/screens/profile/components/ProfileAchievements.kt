package com.example.wayspot.ui.screens.profile.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.wayspot.R
import com.example.wayspot.data.model.achievement.AchievementProgress
import com.example.wayspot.data.model.achievement.AchievementType

/** Insignias del usuario basadas en las estadísticas reales de sus reseñas. */
@Composable
fun ProfileAchievements(
    achievements: List<AchievementProgress>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(top = 8.dp, bottom = 16.dp)
            .testTag("profileAchievements")
    ) {
        Text(
            text = stringResource(R.string.achievements_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Text(
            text = stringResource(
                R.string.achievements_summary,
                achievements.count { it.isUnlocked },
                achievements.size
            ),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            achievements.forEach { achievement ->
                AchievementCard(achievement)
            }
        }
    }
}

@Composable
private fun AchievementCard(progress: AchievementProgress) {
    val title = when (progress.type) {
        AchievementType.FIRST_REVIEW -> R.string.achievement_first_review
        AchievementType.ACTIVE_EXPLORER -> R.string.achievement_active_explorer
        AchievementType.PHOTOGRAPHER -> R.string.achievement_photographer
        AchievementType.INFLUENCER -> R.string.achievement_influencer
        AchievementType.ADVENTURER -> R.string.achievement_adventurer
    }
    val description = when (progress.type) {
        AchievementType.FIRST_REVIEW -> R.string.achievement_first_review_description
        AchievementType.ACTIVE_EXPLORER -> R.string.achievement_active_explorer_description
        AchievementType.PHOTOGRAPHER -> R.string.achievement_photographer_description
        AchievementType.INFLUENCER -> R.string.achievement_influencer_description
        AchievementType.ADVENTURER -> R.string.achievement_adventurer_description
    }
    val icon: ImageVector = when (progress.type) {
        AchievementType.FIRST_REVIEW -> Icons.Filled.EmojiEvents
        AchievementType.ACTIVE_EXPLORER -> Icons.Filled.Explore
        AchievementType.PHOTOGRAPHER -> Icons.Filled.CameraAlt
        AchievementType.INFLUENCER -> Icons.Filled.Favorite
        AchievementType.ADVENTURER -> Icons.Filled.Place
    }

    Card(
        modifier = Modifier
            .width(172.dp)
            .testTag("achievement_${progress.type.name}"),
        colors = CardDefaults.cardColors(
            containerColor = if (progress.isUnlocked) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (progress.isUnlocked) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
            Text(
                text = stringResource(title),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = stringResource(description),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                minLines = 2,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(2.dp))
            LinearProgressIndicator(
                progress = { progress.fraction },
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${progress.current.coerceAtMost(progress.target)}/${progress.target}",
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    text = stringResource(
                        if (progress.isUnlocked) R.string.achievement_unlocked
                        else R.string.achievement_locked
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (progress.isUnlocked) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }
        }
    }
}
