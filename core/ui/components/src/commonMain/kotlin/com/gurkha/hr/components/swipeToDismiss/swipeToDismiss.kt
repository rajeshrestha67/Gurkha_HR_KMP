package com.gurkha.hr.components.swipeToDismiss

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.horizontalDrag
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRightAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.unit.IntOffset
import com.gurkha.hr.components.dimens
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

@Composable
fun SwipeToDismissBox(
    text: String,
    modifier: Modifier = Modifier,
    onDismissed: () -> Unit,
    backgroundColor: Color = MaterialTheme.colorScheme.primary
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(height = MaterialTheme.dimens.swipeToDismissHeight)
            .padding(
                horizontal = MaterialTheme.dimens.small3,
                vertical = MaterialTheme.dimens.small2
            )
            .background(
                color = backgroundColor,
                shape = MaterialTheme.shapes.medium
            )
            .padding(
                start = MaterialTheme.dimens.small2,
                top = MaterialTheme.dimens.small2,
                bottom = MaterialTheme.dimens.small2
            )
            .clipToBounds()
            .swipeToDismiss(onDismissed),
        horizontalArrangement = Arrangement.spacedBy(
            space = MaterialTheme.dimens.medium3,
            alignment = Alignment.Start
        ),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Icon(
            modifier = Modifier
                .padding(horizontal = MaterialTheme.dimens.small1)
                .size(size = MaterialTheme.dimens.medium3)
                .background(
                    color = MaterialTheme.colorScheme.onPrimary,
                    shape = MaterialTheme.shapes.medium
                ),
            imageVector = Icons.AutoMirrored.Filled.ArrowRightAlt,
            contentDescription = text,
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

fun Modifier.swipeToDismiss(
    onDismissed: () -> Unit
): Modifier = composed {
    val offsetX = remember { Animatable(0f) }
    pointerInput(Unit) {

        val decay = splineBasedDecay<Float>(this)

        coroutineScope {
            while (true) {
                val velocityTracker = VelocityTracker()

                offsetX.stop()
                awaitPointerEventScope {

                    val pointerId = awaitFirstDown().id

                    horizontalDrag(pointerId) { change ->
                        launch {
                            offsetX.snapTo(
                                offsetX.value + change.positionChange().x
                            )
                        }
                        velocityTracker.addPosition(
                            change.uptimeMillis,
                            change.position
                        )
                    }
                }
                val velocity = velocityTracker.calculateVelocity().x
                val targetOffsetX = decay.calculateTargetValue(
                    offsetX.value,
                    velocity
                )
                offsetX.updateBounds(
                    lowerBound = -size.width.toFloat(),
                    upperBound = size.width.toFloat()
                )
                launch {
                    offsetX.animateTo(
                        targetValue = 0f,
                        initialVelocity = velocity
                    )
                    if (targetOffsetX.absoluteValue >= size.width * 0.6f) {
                        onDismissed()
                    }
                }
            }
        }
    }.offset { IntOffset(offsetX.value.roundToInt(), 0) }
}

