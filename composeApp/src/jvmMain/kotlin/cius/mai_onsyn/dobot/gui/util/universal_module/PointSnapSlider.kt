package cius.mai_onsyn.dobot.gui.util.universal_module

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import cius.mai_onsyn.dobot.gui.util.interaction
import kotlin.math.roundToInt

@Composable
fun PointSnapSlider(
    points: List<Float>,
    value: Float = points[points.size / 2],
    onChanged: (Float) -> Unit = {},
    modifier: Modifier = Modifier,
    track: @Composable BoxScope.() -> Unit = {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
        )
    },
    thumb: @Composable BoxScope.() -> Unit = {
        Box(
            modifier = Modifier
                .size(24.dp)
                .shadow(2.dp, CircleShape)
                .background(MaterialTheme.colorScheme.primary, CircleShape)
        )
    },
    point: @Composable BoxScope.(isSelected: Boolean) -> Unit = { isSelected ->
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(
                    if (isSelected) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.outline,
                    CircleShape
                )
        )
    },
    text: @Composable BoxScope.(Float) -> Unit = { valValue ->
        Text(
            text = valValue.toString(),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelMedium
        )
    }
) {
    val sortedPoints = remember(points) { points.sorted() }
//    val currentIndex = sortedPoints.indexOf(value).coerceAtLeast(0)

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp) // 预留空间给文字和 Thumb
    ) {
        val widthPx = constraints.maxWidth.toFloat()
        // 留出边距，防止 Thumb 在两端超出边界
        val horizontalPadding = 12.dp
        val paddingPx = with(LocalDensity.current) { horizontalPadding.toPx() }
        val usableWidthPx = widthPx - (paddingPx * 2)

        // 核心逻辑：将 X 坐标转换为最近的点
        var currentIndex by remember { mutableIntStateOf(sortedPoints.indexOf(value)) }
        fun updateNearestValue(x: Float) {
//            println(x)
            if (sortedPoints.isEmpty()) return
            // 将相对于组件左侧的坐标 x，映射到 usableWidth 范围内
            val relativeX = (x - paddingPx).coerceIn(0f, usableWidthPx)
            val fraction = if (usableWidthPx > 0) relativeX / usableWidthPx else 0f

            // 计算索引：fraction * (总数 - 1)
            val rawIndex = (fraction * (sortedPoints.size - 1)).roundToInt()
            val index = rawIndex.coerceIn(0, sortedPoints.size - 1)
            if (index != currentIndex) {
                currentIndex = index
                onChanged(sortedPoints[currentIndex])
            }
        }

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .interaction(
                    onPress = { event -> updateNearestValue(event.changes.first().position.x) },
                    onDrag = { event -> updateNearestValue(event.changes.first().position.x) },
                    onClick = { event -> updateNearestValue(event.changes.first().position.x) }
                ),
            contentAlignment = Alignment.CenterStart
        ) {

            // 1. 轨道层
            BoxWithConstraints(modifier = Modifier.padding(horizontal = horizontalPadding).fillMaxWidth()) {
                track()
            }

            // 2. 刻度点与文字层
            sortedPoints.forEachIndexed { index, pointValue ->
                val fraction = if (sortedPoints.size > 1) index.toFloat() / (sortedPoints.size - 1) else 0.5f
                val xOffset = with(LocalDensity.current) { (paddingPx + fraction * usableWidthPx).toDp() }

                Box(
                    modifier = Modifier
                        .offset(x = xOffset - 15.dp) // 这里的 15.dp 是 Box 宽度的一半，用于居中
                        .width(30.dp),
                    contentAlignment = Alignment.Center
                ) {
                    point(index == currentIndex)
                    Box(modifier = Modifier.padding(top = 45.dp)) {
                        text(pointValue)
                    }
                }
            }

            // 3. 游标层 (Thumb)
            val thumbFraction = if (sortedPoints.size > 1) currentIndex.toFloat() / (sortedPoints.size - 1) else 0.5f
            val thumbXOffset = with(LocalDensity.current) { (paddingPx + thumbFraction * usableWidthPx).toDp() }

            BoxWithConstraints(
                modifier = Modifier
                    .offset(x = thumbXOffset - 12.dp) // 12.dp 是默认 24.dp Thumb 的半径
                    .zIndex(1f), // 确保在最上层
                contentAlignment = Alignment.Center
            ) {
                thumb()
            }
        }
    }
}