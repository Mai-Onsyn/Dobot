package cius.mai_onsyn.dobot.gui.content.experience.safe_state

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cius.mai_onsyn.dobot.gui.ROUND_CORNER_SHAPE
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.unit.sp


@Composable
fun SafeLable(name: String, isConnected: Boolean=true,modifier: Modifier =Modifier) {
    val statusText = if (isConnected) "正常" else "故障"
    val statusColor = if (isConnected) Color(62,166,93) else Color.Red
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
            Text(text=name,
                modifier = Modifier,
                color=MaterialTheme.colorScheme.onSurface,
                fontSize = 12.sp)
            Spacer(modifier = Modifier.weight(1.5f))
            Text(text=statusText,color=statusColor, fontSize = 12.sp)
        }

    }
}