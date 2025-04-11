package com.example.lostintravel.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    // Removed SignUp and ForgotPassword screens
    object Home : Screen("home")
    object Search : Screen("search")
    object Favorites : Screen("favorites")
    object Profile : Screen("profile")
    object Settings : Screen("settings")
    
    object DestinationDetail : Screen("destination/{$ARG_DESTINATION_ID}") {
        const val ARG_DESTINATION_ID = "destinationId"
        
        fun createRoute(destinationId: String): String {
            return "destination/$destinationId"
        }
    }
}