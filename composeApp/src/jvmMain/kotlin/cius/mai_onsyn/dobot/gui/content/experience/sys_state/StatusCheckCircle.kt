import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cius.mai_onsyn.dobot.gui.RED_COLOR

@Composable
fun StatusCheckCircle(isConnected: Boolean = true, modifier: Modifier = Modifier) {
    val color = if (isConnected) Color(62,166,93) else RED_COLOR

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 圆环部分（保持不变）
        Box(
            modifier = Modifier
                .size(96.dp)
                .border(8.dp, color, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(56.dp)) {
                val strokeWidth = 10.dp.toPx()
                if (isConnected) {
                    val path = Path().apply {
                        moveTo(size.width * 0.15f, size.height * 0.5f)
                        lineTo(size.width * 0.45f, size.height * 0.8f)
                        lineTo(size.width * 0.85f, size.height * 0.2f)
                    }
                    drawPath(path, color = color,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round, join = StrokeJoin.Round))
                } else {
                    val path = Path().apply {
                        moveTo(size.width * 0.2f, size.height * 0.2f)
                        lineTo(size.width * 0.8f, size.height * 0.8f)
                        moveTo(size.width * 0.8f, size.height * 0.2f)
                        lineTo(size.width * 0.2f, size.height * 0.8f)
                    }
                    drawPath(path, color = color,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round, join = StrokeJoin.Round))
                }
            }
        }

        // ✨ 文本区域，根据状态切换
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = if (isConnected) "系统正常" else "连接中断",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = if (isConnected) "可以开始实验" else "请检查连接",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}