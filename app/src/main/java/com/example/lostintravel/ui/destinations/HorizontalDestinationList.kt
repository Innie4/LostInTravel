package com.example.lostintravel.ui.destinations

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.lostintravel.domain.model.Destination
import com.example.lostintravel.ui.common.EmptyState
import com.example.lostintravel.ui.common.ErrorState
import com.example.lostintravel.ui.common.LoadingIndicator
import com.example.lostintravel.ui.common.StateHandler
import com.example.lostintravel.ui.common.UiState
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Brush

/**
 * A composable that displays a horizontal list of destinations with a title
 */
@Composable
fun DestinationCategory(
    title: String,
    state: UiState<List<Destination>>,
    onDestinationClick: (Destination) -> Unit,
    onFavoriteClick: (String) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    onSeeAllClick: (() -> Unit)? = null
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
            
            if (onSeeAllClick != null) {
                Text(
                    text = "See All",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { onSeeAllClick() }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        StateHandler(
            state = state,
            modifier = Modifier.height(280.dp),
            loadingContent = { 
                Box(modifier = Modifier.fillMaxWidth()) {
                    LoadingIndicator(modifier = Modifier.align(Alignment.Center))
                }
            },
            emptyContent = {
                EmptyState(
                    title = "No Destinations",
                    message = "No destinations available in this category.",
                    modifier = Modifier.fillMaxWidth()
                )
            },
            errorContent = { message, _ ->
                ErrorState(
                    message = message,
                    onRetry = onRetry,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        ) { destinations ->
            HorizontalDestinationList(
                destinations = destinations,
                onDestinationClick = onDestinationClick,
                onFavoriteClick = onFavoriteClick
            )
        }
    }
}

/**
 * A composable that displays a horizontal list of destinations
 */
@Composable
fun HorizontalDestinationList(
    destinations: List<Destination>,
    onDestinationClick: (Destination) -> Unit,
    onFavoriteClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(
            items = destinations,
            key = { it.id }
        ) { destination ->
            HorizontalDestinationItem(
                destination = destination,
                onClick = { onDestinationClick(destination) },
                onFavoriteClick = { onFavoriteClick(destination.id) }
            )
        }
    }
}

/**
 * A composable that displays a single destination item in a horizontal list
 */
@Composable
fun HorizontalDestinationItem(
    destination: Destination,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(220.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            Box {
                AsyncImage(
                    model = destination.imageUrl,
                    contentDescription = destination.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                )
                
                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .size(32.dp)
                ) {
                    Icon(
                        imageVector = if (destination.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = if (destination.isFavorite) "Remove from favorites" else "Add to favorites",
                        tint = if (destination.isFavorite) Color.Red else Color.White
                    )
                }
            }
            
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = destination.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "${destination.city}, ${destination.country}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        modifier = Modifier.size(16.dp),
                        tint = Color(0xFFFFC107)
                    )
                    
                    Spacer(modifier = Modifier.width(4.dp))
                    
                    Text(
                        text = "${destination.rating}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Text(
                        text = destination.priceLevel,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = destination.description,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * A composable that displays a section for featured destinations
 */
@Composable
fun FeaturedDestinationSection(
    state: UiState<List<Destination>>,
    onDestinationClick: (Destination) -> Unit,
    onFavoriteClick: (String) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    StateHandler(
        state = state,
        modifier = modifier,
        loadingContent = { 
            Box(modifier = Modifier.fillMaxWidth().height(200.dp)) {
                LoadingIndicator(modifier = Modifier.align(Alignment.Center))
            }
        },
        emptyContent = {
            EmptyState(
                title = "No Featured Destinations",
                message = "Check back later for featured destinations.",
                modifier = Modifier.fillMaxWidth().height(200.dp)
            )
        },
        errorContent = { message, _ ->
            ErrorState(
                message = message,
                onRetry = onRetry,
                modifier = Modifier.fillMaxWidth().height(200.dp)
            )
        }
    ) { destinations ->
        if (destinations.isNotEmpty()) {
            FeaturedDestinationCarousel(
                destinations = destinations,
                onDestinationClick = onDestinationClick,
                onFavoriteClick = onFavoriteClick
            )
        }
    }
}

/**
 * A composable that displays a carousel of featured destinations
 */
@Composable
fun FeaturedDestinationCarousel(
    destinations: List<Destination>,
    onDestinationClick: (Destination) -> Unit,
    onFavoriteClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Implementation of a pager-based carousel would go here
    // For simplicity, we'll use a LazyRow with larger items
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(
            items = destinations.take(5), // Limit to 5 featured destinations
            key = { it.id }
        ) { destination ->
            FeaturedDestinationItem(
                destination = destination,
                onClick = { onDestinationClick(destination) },
                onFavoriteClick = { onFavoriteClick(destination.id) }
            )
        }
    }
}

/**
 * A composable that displays a single featured destination item
 */
@Composable
fun FeaturedDestinationItem(
    destination: Destination,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(300.dp)
            .height(200.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box {
            AsyncImage(
                model = destination.imageUrl,
                contentDescription = destination.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth()
            )
            
            // Gradient overlay for better text visibility
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            ),
                            startY = 0f,
                            endY = 400f
                        )
                    )
            )
            
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(
                    text = destination.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "${destination.city}, ${destination.country}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        modifier = Modifier.size(16.dp),
                        tint = Color(0xFFFFC107)
                    )
                    
                    Spacer(modifier = Modifier.width(4.dp))
                    
                    Text(
                        text = "${destination.rating}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )
                }
            }
            
            IconButton(
                onClick = onFavoriteClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = if (destination.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = if (destination.isFavorite) "Remove from favorites" else "Add to favorites",
                    tint = if (destination.isFavorite) Color.Red else Color.White
                )
            }
        }
    }
}