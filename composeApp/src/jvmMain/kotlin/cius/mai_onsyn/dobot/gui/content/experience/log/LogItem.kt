package cius.mai_onsyn.dobot.gui.content.experience.log

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Text
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cius.mai_onsyn.dobot.gui.ROUND_CORNER_SHAPE
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.core.LogEvent
import java.text.SimpleDateFormat

private val TIME_WIDTH = 120.dp
private val THREAD_WIDTH = 110.dp
private val LEVEL_WIDTH = 50.dp
private val SPACING = 10.dp
private val dateFormat = SimpleDateFormat("yyyy-mm-dd HH:mm:ss")
private val FONT_SIZE = 13.sp

@Composable
fun LogItem(
    event: LogEvent,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
    ) {
        Text(
            text = dateFormat.format(event.timeMillis),
            fontSize = FONT_SIZE,
            color = Color(57, 118, 229),
            modifier = Modifier
//                .width(TIME_WIDTH)
        )
        // 线程显示
//        Spacer(modifier = Modifier.width(SPACING))
//        Text(
//            text = event.threadName,
//            fontSize = FONT_SIZE,
//            color = Color(166, 138, 13),
//            modifier = Modifier
//                .width(THREAD_WIDTH)
//        )
        Spacer(modifier = Modifier.width(SPACING))
        Box(
            modifier = Modifier
                .width(LEVEL_WIDTH)
        ) {
            val color = if (event.level == Level.INFO) Color(62, 166, 93)
                        else event.level.color(MaterialTheme.colorScheme)
            Text(
                text = event.level.name(),
                fontSize = FONT_SIZE,
                color = color,
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(color.copy(0.2f))
                    .padding(horizontal = 4.dp)
            )
        }
        Spacer(modifier = Modifier.width(SPACING))
        SelectionContainer {
            Text(
                text = event.message.formattedMessage,
                fontSize = FONT_SIZE,
                color = event.level.color(MaterialTheme.colorScheme),
                modifier = Modifier
            )
        }
    }
}

private fun Level.color(colorScheme: ColorScheme): Color {
    return when (this) {
        Level.DEBUG -> colorScheme.onSurfaceVariant
        Level.INFO -> colorScheme.onSurface
        Level.WARN -> Color(255, 165, 0)
        Level.ERROR -> Color.Red
        else -> colorScheme.onSurface
    }
}