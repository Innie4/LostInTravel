package com.example.lostintravel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.lostintravel.ui.components.BottomNavItem
import com.example.lostintravel.ui.components.LostInTravelBottomNavigation
import com.example.lostintravel.ui.navigation.AppNavigation
import com.example.lostintravel.ui.theme.LostInTravelTheme
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.delay

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    @Inject
    lateinit var googleSignInClient: GoogleSignInClient
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LostInTravelTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route ?: BottomNavItem.HOME.route
                    
                    var showBottomBar by remember { mutableStateOf(true) }
                    var bottomBarVisible by remember { mutableStateOf(false) }
                    
                    // Determine if we should show the bottom bar based on the current route
                    showBottomBar = when {
                        currentRoute.startsWith("login") -> false
                        currentRoute.startsWith("detail/") -> false
                        else -> true
                    }
                    
                    // Animate bottom bar appearance
                    LaunchedEffect(showBottomBar) {
                        if (showBottomBar) {
                            delay(300) // Delay to allow screen transition to complete
                            bottomBarVisible = true
                        } else {
                            bottomBarVisible = false
                        }
                    }
                    
                    Scaffold(
                        bottomBar = {
                            AnimatedVisibility(
                                visible = bottomBarVisible,
                                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
                            ) {
                                if (showBottomBar) {
                                    LostInTravelBottomNavigation(
                                        currentRoute = currentRoute,
                                        onItemClick = { item ->
                                            if (currentRoute != item.route) {
                                                navController.navigate(item.route) {
                                                    popUpTo(navController.graph.startDestinationId) {
                                                        saveState = true
                                                    }
                                                    launchSingleTop = true
                                                    restoreState = true
                                                }
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    ) { innerPadding ->
                        AppNavigation(
                            googleSignInClient = googleSignInClient,
                            navController = navController,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}