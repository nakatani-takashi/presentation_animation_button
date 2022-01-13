package com.example.presentation_animation_button.extension

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

fun Modifier.animationClickable(
    interactionSource: MutableInteractionSource,
    onClick: () -> Unit,
    enabled: Boolean = true
): Modifier = composed {

    var playedAnimation by remember {
        mutableStateOf(false)
    }
    var f by remember {
        mutableStateOf(1.0f)
    }
    val scale by
    animateFloatAsState(
        targetValue = if (playedAnimation) 0.9f else f,
        animationSpec = repeatable(
            iterations = 2,
            animation = tween(durationMillis = 300),
        ),
    )

    scale(scale)
        .pointerInput(interactionSource, enabled) {
            if (enabled) {
                forEachGesture {
                    coroutineScope {
                        awaitPointerEventScope {
                            val down = awaitFirstDown(requireUnconsumed = false)
                            val downPress = PressInteraction.Press(down.position)
                            val holdButtonJob = launch {
                                interactionSource.emit(downPress)
                                f = 0.9f
                                playedAnimation = true
                            }
                            val up = waitForUpOrCancellation()
                            holdButtonJob.cancel()
                            launch {
                                when (up) {
                                    null -> {
                                        interactionSource.emit(PressInteraction.Cancel(downPress))
                                    }
                                    else -> {
                                        interactionSource.emit(PressInteraction.Release(downPress))
                                        onClick()
                                    }
                                }
                                f = 1.0f
                                playedAnimation = false
                            }
                        }
                    }
                }
            }
        }
        .indication(interactionSource, rememberRipple())
}