package com.example.wayspot.ui.screens.userconnections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.wayspot.R
import com.example.wayspot.data.local.PreviewData
import com.example.wayspot.data.model.user.UserRelationshipListType
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.screens.userconnections.components.UserConnectionListItem
import com.example.wayspot.ui.screens.userconnections.components.UserConnectionsEmptyState
import com.example.wayspot.ui.screens.userconnections.components.UserConnectionsHeader
import com.example.wayspot.ui.screens.userconnections.components.UserConnectionsTabs
import com.example.wayspot.ui.theme.WayspotTheme

@Composable
fun UserConnectionsScreen(
    viewModel: UserConnectionsViewModel,
    userId: String,
    initialType: UserRelationshipListType,
    onBackClick: () -> Unit,
    onUserClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(userId, initialType) {
        viewModel.loadConnections(userId, initialType)
    }

    UserConnectionsContent(
        state = state,
        onBackClick = onBackClick,
        onTypeSelected = viewModel::selectType,
        onUserClick = onUserClick,
        onRetry = viewModel::retry,
        modifier = modifier
    )
}

@Composable
fun UserConnectionsContent(
    state: UserConnectionsState,
    onBackClick: () -> Unit,
    onTypeSelected: (UserRelationshipListType) -> Unit,
    onUserClick: (String) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val title = stringResource(
        when (state.selectedType) {
            UserRelationshipListType.FOLLOWERS -> R.string.user_connections_title_followers
            UserRelationshipListType.FOLLOWING -> R.string.user_connections_title_following
        }
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item(key = "connections-header") {
            UserConnectionsHeader(
                title = title,
                onBackClick = onBackClick
            )
        }
        item(key = "connections-tabs") {
            UserConnectionsTabs(
                selectedType = state.selectedType,
                onTypeSelected = onTypeSelected
            )
        }

        if (state.isLoading && state.users.isEmpty()) {
            item(key = "connections-loading") {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CircularProgressIndicator()
                    Text(
                        text = stringResource(R.string.user_connections_loading),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        if (state.hasLoadError) {
            item(key = "connections-error") {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.user_connections_load_error),
                        color = MaterialTheme.colorScheme.error
                    )
                    TextButton(onClick = onRetry) {
                        Text(text = stringResource(R.string.user_connections_retry))
                    }
                }
            }
        }

        if (state.hasLoaded && state.users.isEmpty() && !state.isLoading && !state.hasLoadError) {
            item(key = "connections-empty") {
                UserConnectionsEmptyState(selectedType = state.selectedType)
            }
        }

        items(
            items = state.users,
            key = { user -> user.id }
        ) { user ->
            UserConnectionListItem(
                user = user,
                onClick = { onUserClick(user.id) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@WayspotMultiPreview
@Composable
private fun UserConnectionsPreview() {
    WayspotTheme {
        UserConnectionsContent(
            state = UserConnectionsState(
                userId = "preview-user",
                selectedType = UserRelationshipListType.FOLLOWERS,
                users = PreviewData.connectionUsers
            ),
            onBackClick = {},
            onTypeSelected = {},
            onUserClick = {},
            onRetry = {}
        )
    }
}
