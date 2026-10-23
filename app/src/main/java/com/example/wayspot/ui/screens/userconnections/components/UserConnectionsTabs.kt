package com.example.wayspot.ui.screens.userconnections.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wayspot.R
import com.example.wayspot.data.model.user.UserRelationshipListType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserConnectionsTabs(
    selectedType: UserRelationshipListType,
    onTypeSelected: (UserRelationshipListType) -> Unit,
    modifier: Modifier = Modifier
) {
    val types = remember {
        listOf(
            UserRelationshipListType.FOLLOWERS,
            UserRelationshipListType.FOLLOWING
        )
    }
    val selectedIndex = types.indexOf(selectedType).coerceAtLeast(0)

    PrimaryTabRow(
        selectedTabIndex = selectedIndex,
        modifier = modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.primary,
        indicator = {
            TabRowDefaults.PrimaryIndicator(
                modifier = Modifier
                    .tabIndicatorOffset(selectedIndex)
                    .clip(RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp)),
                color = MaterialTheme.colorScheme.primary,
                height = 3.dp
            )
        },
        divider = {
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
                thickness = 1.dp
            )
        }
    ) {
        types.forEach { type ->
            val isSelected = type == selectedType
            Tab(
                selected = isSelected,
                onClick = { onTypeSelected(type) },
                text = {
                    Text(
                        text = when (type) {
                            UserRelationshipListType.FOLLOWERS -> stringResource(R.string.profile_label_followers)
                            UserRelationshipListType.FOLLOWING -> stringResource(R.string.profile_label_following)
                        },
                        fontSize = 15.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                }
            )
        }
    }
}
