package cius.mai_onsyn.dobot.gui.content.experience.flow

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import cius.mai_onsyn.dobot.gui.BLUE_COLOR
import cius.mai_onsyn.dobot.gui.GREEN_COLOR
import cius.mai_onsyn.dobot.gui.WHITE_COLOR
import dobot.composeapp.generated.resources.Res
import dobot.composeapp.generated.resources.icon_complete
import org.jetbrains.compose.resources.painterResource
import kotlin.repeat

@Composable
fun FlowGraph(
    items: List<String>,
    current: Int = 0,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    val itemHeight = 40.dp
    val circleRadius = 11.dp
    val paddingHeight = 12.dp
    val leftPadding = 6.dp

    val passedColor = GREEN_COLOR
    val currentColor = BLUE_COLOR

    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .drawWithContent {
                repeat(items.size - 1) { index ->
                    drawLine(
                        color = if (index < current - 1) passedColor else if (index == current || index == current - 1) currentColor else colorScheme.outline,
                        start = Offset((circleRadius + leftPadding).toPx(), (circleRadius + index * (itemHeight + paddingHeight)).toPx()),
                        end = Offset((circleRadius + leftPadding).toPx(), (circleRadius + (index + 1) * (itemHeight + paddingHeight)).toPx()),
                    )
                }
                drawContent()
            }
    ) {
        items.forEachIndexed { index, item ->
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .height(itemHeight)
                    .fillMaxWidth()
                    .then(if (index == current) Modifier.background(currentColor.copy(0.2f)) else Modifier)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxHeight().padding(start = leftPadding)
                ) {
                    Box(
                        modifier = Modifier
                            .size(circleRadius * 2)
                            .clip(CircleShape)
                            .background(if (index < current) passedColor else if (index == current) currentColor else colorScheme.outline)
                    ) {
                        Text(
                            text = "${index + 1}",
                            color = WHITE_COLOR,
                            textAlign = TextAlign.Center,
                            fontSize = 12.sp,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = item,
                        color = colorScheme.onSurface,
                        fontSize = 13.sp
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxHeight().align(Alignment.CenterEnd).padding(end = leftPadding)
                ) {
                    Text(
                        text = if (index < current) "已完成" else if (index == current) "进行中" else "等待中",
                        color = if (index != current) colorScheme.onSurfaceVariant else currentColor,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    if (index < current) {
                        Icon(
                            painter = painterResource(Res.drawable.icon_complete),
                            contentDescription = null,
                            tint = passedColor,
                            modifier = Modifier.size(12.dp)
                        )
                    } else if (index == current) {
                        LoadingSpinner(
                            modifier = Modifier.size(12.dp),
                            color = currentColor,
                            strokeWidth = 2.dp,
                            durationMillis = 2000
                        )
                    }
                }
            }
            if (index < items.size - 1) {
                Spacer(modifier = Modifier.height(paddingHeight))
            }
        }
    }
}