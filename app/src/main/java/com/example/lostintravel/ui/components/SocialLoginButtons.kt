package com.example.lostintravel.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.lostintravel.R

@Composable
fun SocialLoginButtons(
    onGoogleClick: () -> Unit,
    onFacebookClick: () -> Unit,
    onTwitterClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        SocialButton(
            iconResId = R.drawable.ic_twitter,
            contentDescription = "Login with Twitter",
            onClick = onTwitterClick
        )
        
        SocialButton(
            iconResId = R.drawable.ic_google,
            contentDescription = "Login with Google",
            onClick = onGoogleClick
        )
        
        SocialButton(
            iconResId = R.drawable.ic_facebook,
            contentDescription = "Login with Facebook",
            onClick = onFacebookClick
        )
    }
}

@Composable
fun SocialButton(
    iconResId: Int,
    contentDescription: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(60.dp)
            .clip(CircleShape)
            .border(1.dp, Color.LightGray, CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = iconResId),
            contentDescription = contentDescription,
            modifier = Modifier.size(24.dp)
        )
    }
}