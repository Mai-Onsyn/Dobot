package cius.mai_onsyn.dobot.gui.content.experience.flow

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun LoadingSpinner(
    modifier: Modifier = Modifier.size(48.dp),
    color: Color = Color(0xFF6200EE), // 染色的颜色
    strokeWidth: Dp = 6.dp,           // 圆环宽度
    durationMillis: Int = 1000        // 转一圈的时间
) {
    // 旋转动画：从 0 度到 360 度循环
    val transition = rememberInfiniteTransition(label = "loading")
    val rotation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Canvas(modifier = modifier) {
        // 1. 绘制背景圆环（浅色背景，可选）
        drawCircle(
            color = color.copy(alpha = 0.2f),
            style = Stroke(width = strokeWidth.toPx())
        )

        // 2. 绘制旋转的染色弧线
        drawArc(
            color = color,
            startAngle = rotation,      // 起始角度随动画变化
            sweepAngle = 90f,           // 染色部分的长度（90度即 1/4 圆）
            useCenter = false,
            style = Stroke(
                width = strokeWidth.toPx(),
                cap = StrokeCap.Round   // 圆角边缘
            )
        )
    }
}