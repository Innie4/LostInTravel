package com.example.lostintravel.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp

@Composable
fun FadeInContent(
    visible: Boolean,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(durationMillis = 500)),
        exit = fadeOut(animationSpec = tween(durationMillis = 500)),
        content = content
    )
}

@Composable
fun SlideInContent(
    visible: Boolean,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = tween(durationMillis = 300, easing = EaseOutQuad)
        ) + fadeIn(animationSpec = tween(durationMillis = 300)),
        exit = slideOutVertically(
            targetOffsetY = { it },
            animationSpec = tween(durationMillis = 300, easing = EaseInQuad)
        ) + fadeOut(animationSpec = tween(durationMillis = 300)),
        content = content
    )
}

@Composable
fun PulseAnimation(
    pulseFraction: Float = 0.1f,
    content: @Composable (animationProgress: Float) -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1f + pulseFraction,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOutQuad),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    
    content(scale)
}

@Composable
fun FadeInStaggered(
    items: List<Any>,
    staggerDelay: Int = 100,
    content: @Composable (Int, Float) -> Unit
) {
    items.forEachIndexed { index, _ ->
        var visible by remember { mutableStateOf(false) }
        
        LaunchedEffect(Unit) {
            delay(index * staggerDelay.toLong())
            visible = true
        }
        
        val alpha by animateFloatAsState(
            targetValue = if (visible) 1f else 0f,
            animationSpec = tween(durationMillis = 300),
            label = "alpha"
        )
        
        content(index, alpha)
    }
}