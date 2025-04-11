package com.example.lostintravel.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.lostintravel.domain.model.WeatherCondition
import com.example.lostintravel.domain.model.WeatherInfo
import com.example.lostintravel.ui.theme.LocationPin
import com.example.lostintravel.ui.theme.PriceBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    destinationId: String,
    onBackClick: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(destinationId) {
        viewModel.loadDestinationDetails(destinationId)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Destination Detail") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Notification action */ }) {
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
                                contentDescription = "Notifications"
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        when (uiState) {
            is DetailUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            
            is DetailUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (uiState as DetailUiState.Error).message,
                        style = TextStyle(
                            fontSize = 18.sp,
                            color = Color.Red
                        )
                    )
                }
            }
            
            is DetailUiState.Success -> {
                val destination = (uiState as DetailUiState.Success).destination
                val weather = (uiState as DetailUiState.Success).weather
                
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Hero image with badge
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    ) {
                        AsyncImage(
                            model = destination.imageUrl,
                            contentDescription = destination.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        
                        // Frequently visited badge
                        if (destination.isFrequentlyVisited) {
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color.Yellow.copy(alpha = 0.8f))
                                    .padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Frequently Visited",
                                    tint = Color.Black,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Frequently Visited",
                                    style = TextStyle(
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                )
                            }
                        }
                        
                        // Visitor count
                        Text(
                            text = "${destination.visitCount}+ people have explored",
                            style = TextStyle(
                                fontSize = 14.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Medium
                            ),
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(16.dp)
                                .background(
                                    Color.Black.copy(alpha = 0.6f),
                                    RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                        
                        // User avatars
                        LazyRow(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy((-8).dp)
                        ) {
                            items(10) { index ->
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(Color.Gray)
                                        .border(2.dp, Color.White, CircleShape)
                                ) {
                                    // This would be user avatars in a real app
                                }
                            }
                        }
                    }
                    
                    // Destination info
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = destination.name,
                                style = TextStyle(
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            
                            Text(
                                text = "$${destination.price}/person",
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PriceBlue
                                )
                            )
                        }
                        
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Location",
                                tint = LocationPin,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "${destination.city}, ${destination.country}",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    color = Color.Gray
                                ),
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                        
                        Button(
                            onClick = { /* View on map */ },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) {
                            Text(
                                text = "View on map",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }
                        
                        Divider(modifier = Modifier.padding(vertical = 16.dp))
                        
                        // Description
                        Text(
                            text = "Description Destination",
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        
                        Text(
                            text = destination.description,
                            style = TextStyle(
                                fontSize = 16.sp,
                                color = Color.DarkGray,
                                lineHeight = 24.sp
                            ),
                            modifier = Modifier.padding(vertical = 8.dp),
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 3
                        )
                        
                        TextButton(
                            onClick = { /* Read more */ },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text(
                                text = "Read More...",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }
                        
                        Divider(modifier = Modifier.padding(vertical = 16.dp))
                        
                        // Gallery
                        Text(
                            text = "Gallery Photo",
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(4) {
                                AsyncImage(
                                    model = destination.imageUrl,
                                    contentDescription = "Gallery image",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(100.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                )
                            }
                        }
                        
                        Divider(modifier = Modifier.padding(vertical = 16.dp))
                        
                        // Weather forecast
                        Text(
                            text = "Today's weather",
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        
                        if (weather != null) {
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                items(weather.hourly) { hourlyWeather ->
                                    WeatherItem(weatherInfo = hourlyWeather)
                                }
                            }
                        } else {
                            Text(
                                text = "Weather information not available",
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // Book now button
                        Button(
                            onClick = { /* Book now */ },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Book Now",
                                style = TextStyle(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WeatherItem(weatherInfo: WeatherInfo) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(80.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF90CAF9))
            .padding(8.dp)
    ) {
        val icon = when (weatherInfo.condition) {
            WeatherCondition.SUNNY -> Icons.Default.WbSunny
            WeatherCondition.PARTLY_CLOUDY -> Icons.Default.Cloud
            WeatherCondition.CLOUDY -> Icons.Default.Cloud
            WeatherCondition.RAINY -> Icons.Default.Umbrella
            WeatherCondition.STORMY -> Icons.Default.Thunderstorm
        }
        
        Icon(
            imageVector = icon,
            contentDescription = weatherInfo.condition.name,
            tint = Color.White,
            modifier = Modifier.size(32.dp)
        )
        
        Text(
            text = "${weatherInfo.temperature.toInt()}°C",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            ),
            modifier = Modifier.padding(vertical = 4.dp)
        )
        
        Text(
            text = weatherInfo.time,
            style = TextStyle(
                fontSize = 14.sp,
                color = Color.White
            )
        )
    }
}