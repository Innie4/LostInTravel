package com.example.lostintravel.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.lostintravel.domain.model.Destination
import com.example.lostintravel.ui.components.DestinationCard
import com.example.lostintravel.ui.components.SearchBar

@Composable
fun HomeScreen(
    onNavigateToDetail: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    
    var showOnboarding by remember { mutableStateOf(true) }
    
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                
                // Header with profile and welcome message
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Profile image
                    AsyncImage(
                        model = currentUser?.photoUrl ?: "https://via.placeholder.com/150",
                        contentDescription = "Profile",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray)
                    )
                    
                    // Welcome text
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 16.dp)
                    ) {
                        Text(
                            text = "Welcome ${currentUser?.name ?: "Samira"},",
                            style = TextStyle(
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = "Where do you want to go?",
                            style = TextStyle(
                                fontSize = 16.sp,
                                color = Color.Gray
                            )
                        )
                    }
                    
                    // Notification icon
                    IconButton(
                        onClick = { /* Notification logic */ },
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                    ) {
                        BadgedBox(
                            badge = {
                                Badge(
                                    containerColor = Color.Red,
                                    modifier = Modifier.offset(x = (-6).dp, y = 6.dp)
                                )
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
                
                // Search bar
                SearchBar(
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
            
            when (uiState) {
                is HomeUiState.Loading -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
                
                is HomeUiState.Empty -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No destinations found",
                                style = TextStyle(
                                    fontSize = 18.sp,
                                    color = Color.Gray
                                )
                            )
                        }
                    }
                }
                
                is HomeUiState.Success -> {
                    val successState = uiState as HomeUiState.Success
                    
                    // Frequently Visited Section
                    if (successState.frequentlyVisitedDestinations.isNotEmpty()) {
                        item {
                            SectionHeader(
                                title = "Frequently Visited",
                                modifier = Modifier.padding(top = 24.dp, bottom = 16.dp)
                            )
                            
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                contentPadding = PaddingValues(end = 16.dp)
                            ) {
                                items(successState.frequentlyVisitedDestinations) { destination ->
                                    DestinationCard(
                                        destination = destination,
                                        onCardClick = { onNavigateToDetail(it) },
                                        onFavoriteClick = { id, isFavorite ->
                                            viewModel.toggleFavorite(id, isFavorite)
                                        }
                                    )
                                }
                            }
                        }
                    }
                    
                    // Recommended Places Section
                    if (successState.recommendedDestinations.isNotEmpty()) {
                        item {
                            SectionHeader(
                                title = "Recommended Places",
                                showSeeAll = true,
                                onSeeAllClick = { /* See all logic */ },
                                modifier = Modifier.padding(top = 32.dp, bottom = 16.dp)
                            )
                        }
                        
                        items(successState.recommendedDestinations) { destination ->
                            RecommendedDestinationItem(
                                destination = destination,
                                onClick = { onNavigateToDetail(it) },
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                        }
                    }
                }
                
                is HomeUiState.Error -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = (uiState as HomeUiState.Error).message,
                                style = TextStyle(
                                    fontSize = 18.sp,
                                    color = Color.Red
                                )
                            )
                        }
                    }
                }
            }
            
            // Add bottom padding for navigation bar
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
        
        // Onboarding dialog
        if (showOnboarding) {
            OnboardingDialog(
                onDismiss = { showOnboarding = false },
                onWatchNow = { 
                    // Handle watch now action
                    showOnboarding = false
                }
            )
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    showSeeAll: Boolean = false,
    onSeeAllClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        )
        
        if (showSeeAll) {
            TextButton(onClick = onSeeAllClick) {
                Text(
                    text = "Explore All",
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
    }
}

@Composable
fun RecommendedDestinationItem(
    destination: Destination,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable { onClick(destination.id) },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            // Destination image
            AsyncImage(
                model = destination.imageUrl,
                contentDescription = destination.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(100.dp)
                    .fillMaxHeight()
            )
            
            // Destination info
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = destination.name,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Location",
                        tint = Color.Red,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "${destination.city}, ${destination.country}",
                        style = TextStyle(
                            fontSize = 14.sp,
                            color = Color.Gray
                        ),
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
            
            // Price
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = "$${destination.price}/person",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
            }
        }
    }
}

@Composable
fun OnboardingDialog(
    onDismiss: () -> Unit,
    onWatchNow: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Welcome to LostInTravel!!!",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                AsyncImage(
                    model = "https://hebbkx1anhila5yf.public.blob.vercel-storage.com/Home%20new-DONwLVhm1oUuUzlDPlUzPDLZh3JbIj.png", // Using the provided waterfall image
                    contentDescription = "Onboarding Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
                
                Text(
                    text = "Want to know more about us?",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )
                
                Text(
                    text = "let's onboard you on how to use LostInTravel, we help pick the right place and help plan that perfect vacation you need.",
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = onWatchNow,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "Watch Now")
                    }
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        Text(
                            text = "Skip",
                            style = TextStyle(
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                }
            }
        }
    }
}