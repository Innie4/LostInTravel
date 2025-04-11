package com.example.lostintravel.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.lostintravel.ui.auth.AuthViewModel
import com.example.lostintravel.ui.auth.LoginScreen
import com.example.lostintravel.ui.destinations.DestinationDetailScreen
import com.example.lostintravel.ui.favorites.FavoritesScreen
import com.example.lostintravel.ui.home.HomeScreen
import com.example.lostintravel.ui.profile.ProfileScreen
import com.example.lostintravel.ui.search.SearchScreen
import com.example.lostintravel.ui.settings.SettingsScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val isAuthenticated by authViewModel.isAuthenticated.collectAsState(initial = false)
    val startDestination = if (isAuthenticated) Screen.Home.route else Screen.Login.route
    
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // Auth screens
        composable(route = Screen.Login.route) {
            LoginScreen(
                // Remove navigation to signup and forgot password
                onNavigateToHome = { navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }}
            )
        }
        
        // Removed composables for SignUp and ForgotPassword screens
        
        // Main app screens
        composable(route = Screen.Home.route) {
            HomeScreen(
                onDestinationClick = { destinationId ->
                    navController.navigate(Screen.DestinationDetail.createRoute(destinationId))
                }
            )
        }
        
        composable(route = Screen.Search.route) {
            SearchScreen(
                onDestinationClick = { destination ->
                    navController.navigate(Screen.DestinationDetail.createRoute(destination.id))
                }
            )
        }
        
        composable(route = Screen.Favorites.route) {
            FavoritesScreen(
                onDestinationClick = { destination ->
                    navController.navigate(Screen.DestinationDetail.createRoute(destination.id))
                }
            )
        }
        
        composable(route = Screen.Profile.route) {
            ProfileScreen()
        }
        
        composable(route = Screen.DestinationDetail.route) {
            val destinationId = backStackEntry.arguments?.getString(
                Screen.DestinationDetail.ARG_DESTINATION_ID
            ) ?: ""
            
            DestinationDetailScreen(
                destinationId = destinationId,
                onBackClick = { navController.popBackStack() }
            )
        }
        
        composable(route = Screen.Settings.route) {
            SettingsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}