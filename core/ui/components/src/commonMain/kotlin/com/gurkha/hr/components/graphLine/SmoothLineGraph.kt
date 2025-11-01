package com.gurkha.hr.components.graphLine

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toIntRect
import androidx.compose.ui.unit.toSize
import com.gurkha.hr.components.erpColors
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

val graphData = listOf(
    GraphData(65631.00),
    GraphData(65931.00),
    GraphData(65851.00),
    GraphData(65931.00),
    GraphData(66484.00),
    GraphData(67684.00),
    GraphData(66684.00),
    GraphData(66984.00),
    GraphData(70600.00),
    GraphData(71600.00),
    GraphData(72600.00),
    GraphData(72526.00),
    GraphData(72976.00),
    GraphData(73589.00),
)

@Composable
fun SmoothLineGraph() {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val animationProgress = remember { Animatable(0f) }
        var highlightedWeek by remember { mutableStateOf<Int?>(null) }

        LaunchedEffect(key1 = graphData, block = {
            animationProgress.animateTo(1f, tween(3000))
        })

        val coroutineScope = rememberCoroutineScope()
        val textMeasurer = rememberTextMeasurer()
        val labelTextStyle = MaterialTheme.typography.labelSmall
        val outLineColor = MaterialTheme.erpColors.primaryTextColor
        val highLightColor = MaterialTheme.erpColors.highLightColor
        Spacer(
            modifier = Modifier
                .aspectRatio(3f / 2f)
                .fillMaxSize()
                .align(Alignment.Center)
                .pointerInput(Unit) {
                    detectTapGestures {
                        coroutineScope.launch {
                            animationProgress.snapTo(0f)
                            animationProgress.animateTo(1f, tween(3000))
                        }
                    }
                }
                .pointerInput(Unit) {

                    detectDragGesturesAfterLongPress(
                        onDragStart = { offset ->
                            highlightedWeek =
                                (offset.x / (size.width / (graphData.size - 1))).roundToInt()
                        },
                        onDragEnd = { highlightedWeek = null },
                        onDragCancel = { highlightedWeek = null },
                        onDrag = { change, _ ->
                            highlightedWeek =
                                (change.position.x / (size.width / (graphData.size - 1))).roundToInt()
                        }
                    )
                }
                .drawWithCache {
                    val path = generateSmoothPath(graphData, size)
                    val filledPath = Path()
                    filledPath.addPath(path)
                    filledPath.relativeLineTo(0f, size.height)
                    filledPath.lineTo(0f, size.height)
                    filledPath.close()

                    onDrawBehind {
                        val barWidthPx = 1.dp.toPx()
                        drawRect(outLineColor, style = Stroke(barWidthPx))

                        val verticalLines = 4
                        val verticalSize = size.width / (verticalLines + 1)
                        repeat(verticalLines) { i ->
                            val startX = verticalSize * (i + 1)
                            drawLine(
                                outLineColor,
                                start = Offset(startX, 0f),
                                end = Offset(startX, size.height),
                                strokeWidth = barWidthPx
                            )
                        }
                        val horizontalLines = 3
                        val sectionSize = size.height / (horizontalLines + 1)
                        repeat(horizontalLines) { i ->
                            val startY = sectionSize * (i + 1)
                            drawLine(
                                outLineColor,
                                start = Offset(0f, startY),
                                end = Offset(size.width, startY),
                                strokeWidth = barWidthPx
                            )
                        }

                        // draw line
                        clipRect(right = size.width * animationProgress.value) {
                            drawPath(path, Color.Green, style = Stroke(2.dp.toPx()))

                            drawPath(
                                filledPath,
                                brush = Brush.verticalGradient(
                                    listOf(
                                        Color.Green.copy(alpha = 0.4f),
                                        Color.Transparent
                                    )
                                ),
                                style = Fill
                            )
                        }

                        // draw highlight if user is dragging
                        highlightedWeek?.let {
                            this.drawHighlight(
                                highlightedWeek = it,
                                graphData,
                                textMeasurer,
                                labelTextStyle,
                                highLightColor
                            )
                        }


                    }
                }
        )
    }
}


@OptIn(ExperimentalTextApi::class)
fun DrawScope.drawHighlight(
    highlightedWeek: Int,
    graphData: List<GraphData>,
    textMeasurer: TextMeasurer,
    labelTextStyle: TextStyle,
    highlightColor: Color
) {
    val amount = graphData[highlightedWeek].data
    val minAmount = graphData.minBy { it.data }.data
    val range = graphData.maxBy { it.data }.data - minAmount
    val percentageHeight = ((amount - minAmount).toFloat() / range.toFloat())
    val pointY = size.height - (size.height * percentageHeight)
    // draw vertical line on week
    val x = highlightedWeek * (size.width / (graphData.size - 1))
    drawLine(
        highlightColor,
        start = Offset(x, 0f),
        end = Offset(x, size.height),
        strokeWidth = 2.dp.toPx(),
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
    )

    // draw hit circle on graph
    drawCircle(
        Color.Green,
        radius = 4.dp.toPx(),
        center = Offset(x, pointY)
    )

    // draw info box
    val textLayoutResult = textMeasurer.measure("$amount", style = labelTextStyle)
    val highlightContainerSize = (textLayoutResult.size).toIntRect().inflate(4.dp.roundToPx()).size
    val boxTopLeft = (x - (highlightContainerSize.width / 2f))
        .coerceIn(0f, size.width - highlightContainerSize.width)
    drawRoundRect(
        Color.White,
        topLeft = Offset(boxTopLeft, 0f),
        size = highlightContainerSize.toSize(),
        cornerRadius = CornerRadius(8.dp.toPx())
    )
    drawText(
        textLayoutResult,
        color = Color.Black,
        topLeft = Offset(boxTopLeft + 4.dp.toPx(), 4.dp.toPx())
    )
}
