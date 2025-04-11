package com.example.lostintravel.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
) {
    HOME("home", Icons.Default.Home, "Home"),
    EXPLORE("explore", Icons.Default.Search, "Explore"),
    PLAN("plan", Icons.Outlined.Map, "Plan"),
    HISTORY("history", Icons.Outlined.History, "History"),
    PROFILE("profile", Icons.Default.Person, "Profile")
}

@Composable
fun LostInTravelBottomNavigation(
    currentRoute: String,
    onItemClick: (BottomNavItem) -> Unit
) {
    NavigationBar(
        tonalElevation = 8.dp
    ) {
        BottomNavItem.values().forEach { item ->
            LostInTravelNavigationBarItem(
                selected = currentRoute == item.route,
                onClick = { onItemClick(item) },
                icon = item.icon,
                label = item.label
            )
        }
    }
}

@Composable
fun RowScope.LostInTravelNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = label
            )
        },
        label = {
            Text(
                text = label,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        },
        modifier = modifier
    )
}