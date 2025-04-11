package com.example.lostintravel.ui.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

/**
 * Animated visibility with fade and slide animations
 */
@Composable
fun FadeSlideInAnimation(
    visible: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        modifier = modifier,
        enter = fadeIn(animationSpec = tween(300)) + 
                slideInVertically(animationSpec = tween(300)) { height -> height / 2 },
        exit = fadeOut(animationSpec = tween(300)) +
                slideOutVertically(animationSpec = tween(300)) { height -> height / 2 }
    ) {
        content()
    }
}

/**
 * Staggered animation for list items
 */
@Composable
fun StaggeredAnimatedItem(
    itemIndex: Int,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val visibleState = remember {
        MutableTransitionState(false).apply {
            // Start the animation immediately
            targetState = true
        }
    }
    
    // Stagger the animations based on item index
    LaunchedEffect(itemIndex) {
        // Delay based on item index (50ms per item)
        kotlinx.coroutines.delay(itemIndex * 50L)
    }
    
    AnimatedVisibility(
        visibleState = visibleState,
        modifier = modifier,
        enter = fadeIn(animationSpec = tween(300)) + 
                scaleIn(
                    initialScale = 0.9f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ),
        exit = fadeOut() + scaleOut()
    ) {
        content()
    }
}

/**
 * Navigation transitions
 */
object NavigationTransitions {
    val enterTransition: EnterTransition = fadeIn(animationSpec = tween(300)) + 
            slideInVertically(
                initialOffsetY = { 100 },
                animationSpec = tween(300)
            )
            
    val exitTransition: ExitTransition = fadeOut(animationSpec = tween(300)) + 
            slideOutVertically(
                targetOffsetY = { -100 },
                animationSpec = tween(300)
            )
}