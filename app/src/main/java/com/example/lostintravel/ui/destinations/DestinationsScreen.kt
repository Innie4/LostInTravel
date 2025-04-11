package com.example.lostintravel.ui.destinations

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.lostintravel.domain.model.Destination
import com.example.lostintravel.ui.common.EmptyState
import com.example.lostintravel.ui.common.ErrorState
import com.example.lostintravel.ui.common.LoadingIndicator
import com.example.lostintravel.ui.common.StateHandler
import com.example.lostintravel.ui.common.UiState
import com.example.lostintravel.ui.home.HomeViewModel

/**
 * Main screen for displaying destinations
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DestinationsScreen(
    onDestinationClick: (Destination) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.destinationsState.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { viewModel.refreshDestinations() }
    )
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        StateHandler(
            state = uiState,
            loadingContent = { LoadingIndicator() },
            emptyContent = {
                EmptyState(
                    title = "No Destinations Found",
                    message = "We couldn't find any destinations. Try refreshing or check back later.",
                    actionLabel = "Refresh",
                    onAction = { viewModel.refreshDestinations() }
                )
            },
            errorContent = { message, _ ->
                ErrorState(
                    message = message,
                    onRetry = { viewModel.refreshDestinations() }
                )
            }
        ) { destinations ->
            DestinationList(
                destinations = destinations,
                onDestinationClick = onDestinationClick,
                onFavoriteClick = { viewModel.toggleFavorite(it) },
                contentPadding = PaddingValues(16.dp)
            )
        }
        
        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}