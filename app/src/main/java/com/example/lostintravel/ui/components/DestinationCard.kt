package com.example.lostintravel.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
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
import coil.compose.AsyncImage
import com.example.lostintravel.domain.model.Destination
import com.example.lostintravel.ui.theme.LocationPin
import com.example.lostintravel.ui.theme.PriceBlue

@Composable
fun DestinationCard(
    destination: Destination,
    modifier: Modifier = Modifier,
    onCardClick: (String) -> Unit = {},
    onFavoriteClick: (String, Boolean) -> Unit = { _, _ -> }
) {
    var isFavorite by remember { mutableStateOf(false) }
    
    Card(
        modifier = modifier
            .width(280.dp)
            .clickable { onCardClick(destination.id) },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box {
            Column {
                Box(
                    modifier = Modifier
                        .height(180.dp)
                        .fillMaxWidth()
                ) {
                    AsyncImage(
                        model = destination.imageUrl,
                        contentDescription = destination.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    )
                    
                    IconButton(
                        onClick = {
                            isFavorite = !isFavorite
                            onFavoriteClick(destination.id, isFavorite)
                        },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) Color.Red else Color.White
                        )
                    }
                }
                
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = destination.name,
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location",
                            tint = LocationPin,
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
                    
                    Text(
                        text = "$${destination.price}/person",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = PriceBlue
                        ),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}