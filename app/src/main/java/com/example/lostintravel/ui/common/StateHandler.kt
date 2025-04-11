package com.example.lostintravel.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun <T> StateHandler(
    state: UiState<T>,
    loadingContent: @Composable () -> Unit = { LoadingIndicator() },
    errorContent: @Composable (message: String, throwable: Throwable?) -> Unit = { message, _ -> 
        ErrorState(message = message) 
    },
    emptyContent: @Composable () -> Unit = { EmptyState() },
    modifier: Modifier = Modifier,
    content: @Composable (data: T) -> Unit
) {
    when (state) {
        is UiState.Loading -> {
            loadingContent()
        }
        is UiState.Success -> {
            if (isEmptyData(state.data)) {
                emptyContent()
            } else {
                content(state.data)
            }
        }
        is UiState.Error -> {
            errorContent(state.message, state.throwable)
        }
    }
}

private fun <T> isEmptyData(data: T): Boolean {
    return when (data) {
        null -> true
        is List<*> -> data.isEmpty()
        is String -> data.isEmpty()
        is Map<*, *> -> data.isEmpty()
        is Collection<*> -> data.isEmpty()
        else -> false
    }
}