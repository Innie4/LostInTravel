package com.example.lostintravel.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.lostintravel.ui.navigation.AppNavHost
import com.example.lostintravel.ui.theme.LostInTravelTheme
import com.example.lostintravel.util.NetworkConnectivityChecker
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var networkConnectivityChecker: NetworkConnectivityChecker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LostInTravelTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(networkConnectivityChecker)
                }
            }
        }
    }
}

@Composable
fun MainScreen(networkConnectivityChecker: NetworkConnectivityChecker) {
    val snackbarHostState = remember { SnackbarHostState() }
    
    // Observe network connectivity
    val isConnected by networkConnectivityChecker.observeNetworkConnectivity()
        .collectAsState(initial = true)
    
    // Show a snackbar when offline
    LaunchedEffect(isConnected) {
        if (!isConnected) {
            snackbarHostState.showSnackbar(
                message = "You're offline. Showing cached content.",
                duration = androidx.compose.material3.SnackbarDuration.Indefinite
            )
        }
    }
    
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            AppNavHost()
        }
    }
}