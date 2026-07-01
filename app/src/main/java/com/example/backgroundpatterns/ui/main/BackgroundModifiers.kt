package com.example.backgroundpatterns.ui.main

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.sqrt

// 1. Scalloped Pattern (The Optical Illusion from your original image)
fun Modifier.scallopedPatternBackground(
    patternColor: Color,
    circleDiameter: Dp = 16.dp
) = this.drawBehind {
    val diameterPx = circleDiameter.toPx()
    val radiusPx = diameterPx / 2f
    
    clipRect {
        val cols = (size.width / diameterPx).toInt() + 1
        val rows = (size.height / diameterPx).toInt() + 1
        
        for (row in 0..rows) {
            for (col in 0..cols) {
                drawCircle(
                    color = patternColor,
                    radius = radiusPx,
                    center = Offset(
                        x = col * diameterPx + radiusPx,
                        y = row * diameterPx
                    )
                )
            }
        }
    }
}

// 2. Diagonal Stripes (From the first request)
fun Modifier.diagonalStripedBackground(
    stripes: List<Pair<Color, Dp>>
) = this.drawBehind {
    clipRect {
        // Calculate the diagonal length needed to cover the rotated canvas
        val diagonalLength = sqrt(size.width * size.width + size.height * size.height)
        
        // Translate start position to cover the top left corner after rotation
        val startX = (size.width - diagonalLength) / 2f
        val startY = (size.height - diagonalLength) / 2f
        
        var currentOffset = startX
        
        withTransform({ rotate(degrees = 30f) }) {
            while (currentOffset < startX + diagonalLength) {
                stripes.forEach { (color, width) ->
                    val stripeWidthPx = width.toPx()
                    drawRect(
                        color = color,
                        topLeft = Offset(x = currentOffset, y = startY),
                        size = androidx.compose.ui.geometry.Size(width = stripeWidthPx, height = diagonalLength),
                    )
                    currentOffset += stripeWidthPx
                }
            }
        }
    }
}

// 3. Staggered Polka Dots (Hex Grid)
fun Modifier.staggeredPolkaDotBackground(
    dotColor: Color,
    dotRadius: Dp = 4.dp,
    spacing: Dp = 24.dp
) = drawBehind {
    val radiusPx = dotRadius.toPx()
    val spacingPx = spacing.toPx()
    
    // sin(60 degrees) ≈ 0.866. This makes the dots form perfect equilateral triangles.
    val rowHeightPx = spacingPx * 0.866f 
    
    val cols = (size.width / spacingPx).toInt() + 1
    val rows = (size.height / rowHeightPx).toInt() + 1
    
    for (row in 0..rows) {
        // Offset every other row by half the spacing to stagger them
        val staggerOffset = if (row % 2 != 0) spacingPx / 2f else 0f
        
        for (col in 0..cols) {
            drawCircle(
                color = dotColor,
                radius = radiusPx,
                center = Offset(
                    x = col * spacingPx + staggerOffset,
                    y = row * rowHeightPx
                )
            )
        }
    }
}

// 4. Blueprint / Graph Paper Grid
fun Modifier.blueprintBackground(
    gridColor: Color = Color.LightGray,
    cellSize: Dp = 24.dp,
    strokeWidth: Dp = 1.dp
) = drawBehind {
    val cellPx = cellSize.toPx()
    val strokePx = strokeWidth.toPx()
    
    val cols = (size.width / cellPx).toInt() + 1
    val rows = (size.height / cellPx).toInt() + 1
    
    // Draw vertical lines
    for (col in 0..cols) {
        val x = col * cellPx
        drawLine(
            color = gridColor,
            start = Offset(x, 0f),
            end = Offset(x, size.height),
            strokeWidth = strokePx
        )
    }
    
    // Draw horizontal lines
    for (row in 0..rows) {
        val y = row * cellPx
        drawLine(
            color = gridColor,
            start = Offset(0f, y),
            end = Offset(size.width, y),
            strokeWidth = strokePx
        )
    }
}

// 5. Adaptive Wave Header
fun Modifier.waveHeaderBackground(
    color: Color,
    waveAmplitude: Dp = 24.dp
) = drawBehind {
    val amplitudePx = waveAmplitude.toPx()
    
    val path = Path().apply {
        moveTo(0f, 0f) // Start Top Left
        lineTo(size.width, 0f) // Draw to Top Right
        lineTo(size.width, size.height - amplitudePx) // Draw to Bottom Right
        
        // Draw a smooth bezier curve for the wave back to the left side
        cubicTo(
            x1 = size.width * 0.75f, y1 = size.height + amplitudePx,       // Control point 1 (pulls curve down)
            x2 = size.width * 0.25f, y2 = size.height - (amplitudePx * 2), // Control point 2 (pulls curve up)
            x3 = 0f,                 y3 = size.height - amplitudePx        // End point (Bottom Left)
        )
        close() // Connects back to Top Left
    }
    
    drawPath(path = path, color = color)
}

// 6. Checkerboard (Argyle) Grid
fun Modifier.checkerboardBackground(
    color1: Color,
    color2: Color,
    cellSize: Dp = 24.dp
) = drawBehind {
    val cellPx = cellSize.toPx()
    val cols = (size.width / cellPx).toInt() + 1
    val rows = (size.height / cellPx).toInt() + 1

    for (row in 0..rows) {
        for (col in 0..cols) {
            val isEven = (row + col) % 2 == 0
            drawRect(
                color = if (isEven) color1 else color2,
                topLeft = Offset(col * cellPx, row * cellPx),
                size = androidx.compose.ui.geometry.Size(cellPx, cellPx)
            )
        }
    }
}

// 7. Concentric Ripples
fun Modifier.rippleBackground(
    ringColor: Color,
    ringSpacing: Dp = 30.dp,
    strokeWidth: Dp = 2.dp
) = drawBehind {
    val spacingPx = ringSpacing.toPx()
    val strokePx = strokeWidth.toPx()
    val maxRadius = kotlin.math.max(size.width, size.height)
    val ringCount = (maxRadius / spacingPx).toInt() + 1
    
    val centerOffset = Offset(size.width / 2f, size.height / 2f)
    
    for (i in 1..ringCount) {
        drawCircle(
            color = ringColor,
            radius = i * spacingPx,
            center = centerOffset,
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = strokePx)
        )
    }
}

// 8. Crosshatch Woven Pattern
fun Modifier.crosshatchBackground(
    lineColor: Color,
    lineSpacing: Dp = 16.dp,
    strokeWidth: Dp = 1.dp
) = drawBehind {
    val spacingPx = lineSpacing.toPx()
    val strokePx = strokeWidth.toPx()
    val maxDim = kotlin.math.max(size.width, size.height)
    val diagLength = maxDim * 2f
    val lineCount = (diagLength / spacingPx).toInt()
    
    clipRect {
        // Draw diagonal lines top-left to bottom-right
        for (i in -lineCount..lineCount) {
            val offset = i * spacingPx
            drawLine(
                color = lineColor,
                start = Offset(offset, 0f),
                end = Offset(offset + maxDim, maxDim),
                strokeWidth = strokePx
            )
        }
        // Draw diagonal lines bottom-left to top-right
        for (i in -lineCount..lineCount) {
            val offset = i * spacingPx
            drawLine(
                color = lineColor,
                start = Offset(offset, maxDim),
                end = Offset(offset + maxDim, 0f),
                strokeWidth = strokePx
            )
        }
    }
}

// 9. Halftone Gradient Dots
fun Modifier.halftoneBackground(
    dotColor: Color,
    gridSize: Dp = 20.dp
) = drawBehind {
    val gridPx = gridSize.toPx()
    val cols = (size.width / gridPx).toInt() + 1
    val rows = (size.height / gridPx).toInt() + 1
    
    for (row in 0..rows) {
        for (col in 0..cols) {
            val centerX = col * gridPx + gridPx / 2f
            val centerY = row * gridPx + gridPx / 2f
            
            // Dynamic radius based on X position (creates gradient effect)
            val progress = centerX / size.width
            val maxRadius = gridPx / 2f
            val radius = maxRadius * progress
            
            drawCircle(
                color = dotColor,
                radius = radius,
                center = Offset(centerX, centerY)
            )
        }
    }
}

// 10. Sawtooth (Receipt) Edge
fun Modifier.sawtoothHeaderBackground(
    color: Color,
    toothSize: Dp = 16.dp
) = drawBehind {
    val toothPx = toothSize.toPx()
    val toothCount = (size.width / toothPx).toInt() + 1
    
    val path = Path().apply {
        moveTo(0f, 0f)
        lineTo(size.width, 0f)
        lineTo(size.width, size.height - toothPx)
        
        // Draw zigzags right to left
        for (i in toothCount downTo 1) {
            val rightX = i * toothPx
            val leftX = (i - 1) * toothPx
            val midX = leftX + toothPx / 2f
            
            lineTo(midX, size.height) // Point down
            lineTo(leftX, size.height - toothPx) // Point up
        }
        close()
    }
    
    drawPath(path = path, color = color)
}
