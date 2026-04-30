package cius.mai_onsyn.dobot.gui.content.experience.connect

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import cius.mai_onsyn.dobot.gui.ROUND_CORNER_SHAPE
import cius.mai_onsyn.dobot.gui.util.background

@Composable
fun DeviceStatusLabel(name: String, isConnected: Boolean=true,modifier: Modifier=Modifier) {
    val statusText = if (isConnected) "已连接" else "未连接"
    val statusColor = if (isConnected) Color(0xFF2E7D32) else Color.Red
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                shape = ROUND_CORNER_SHAPE
            )
            .border(width = 1.dp, color = MaterialTheme.colorScheme.outline.copy(0.2f), shape = ROUND_CORNER_SHAPE)
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row{
            Text(text= name,
                modifier = Modifier,
                color=MaterialTheme.colorScheme.onSurface)
            Spacer(modifier = Modifier.weight(1.5f))
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .offset(y=5.dp)
                    .background(statusColor, CircleShape)
            )
            Spacer(modifier = Modifier.weight(0.1f))
            Text(text=statusText,color=statusColor)
        }

    }
}