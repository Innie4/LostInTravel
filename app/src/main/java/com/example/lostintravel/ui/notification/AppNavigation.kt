package com.example.lostintravel.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.lostintravel.ui.auth.AuthViewModel
import com.example.lostintravel.ui.auth.LoginScreen
import com.example.lostintravel.ui.detail.DetailScreen
import com.example.lostintravel.ui.home.HomeScreen
import com.google.android.gms.auth.api.signin.GoogleSignInClient

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object Detail : Screen("detail/{destinationId}") {
        fun createRoute(destinationId: String) = "detail/$destinationId"
    }
}

@Composable
fun AppNavigation(
    googleSignInClient: GoogleSignInClient,
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val isUserSignedIn by authViewModel.isUserSignedIn.collectAsState()
    
    NavHost(
        navController = navController,
        startDestination = if (isUserSignedIn) Screen.Home.route else Screen.Login.route,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            )
        }
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToSignUp = { /* Navigate to sign up */ },
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                googleSignInClient = googleSignInClient
            )
        }
        
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToDetail = { destinationId ->
                    navController.navigate(Screen.Detail.createRoute(destinationId))
                }
            )
        }
        
        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument("destinationId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val destinationId = backStackEntry.arguments?.getString("destinationId") ?: ""
            DetailScreen(
                destinationId = destinationId,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}