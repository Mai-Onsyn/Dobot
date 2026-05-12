package cius.mai_onsyn.dobot.gui.content.trajectory.file

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import cius.mai_onsyn.dobot.gui.GLOBAL_PADDING
import cius.mai_onsyn.dobot.gui.ROUND_SMALL_CORNER_SHAPE
import cius.mai_onsyn.dobot.gui.util.buttonEffect
import cius.mai_onsyn.dobot.gui.util.interaction
import cius.mai_onsyn.dobot.gui.util.tweenSpecColor
import cius.mai_onsyn.dobot.gui.util.universal_module.GenericButton
import cius.mai_onsyn.dobot.gui.util.universal_module.PopupContextItem
import cius.mai_onsyn.dobot.gui.util.universal_module.layout.AttachedPopup
import com.alibaba.fastjson2.JSONArray
import java.io.File
import java.nio.file.Files
import kotlin.math.roundToInt

@Composable
fun FileItem(
    modifier: Modifier = Modifier,
    filePath: String,
    selected: Boolean = false,
    onSelect: (String) -> Unit = {}
) {
    val file = File(filePath)
    if (!file.exists()) return
    val json = JSONArray.parseArray(Files.readString(file.toPath()))
    val background by animateColorAsState(
        if (selected) MaterialTheme.colorScheme.primary.copy(0.7f) else Color.Transparent,
        tweenSpecColor
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(background)
            .buttonEffect(
                hoverAlpha = 0.05f,
                pressedAlpha = 0.1f
            )
            .padding(horizontal = GLOBAL_PADDING)
            .interaction(
                onClick = { onSelect(filePath) },
            )
    ) {
        val contentColor by animateColorAsState(
            if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
            tweenSpecColor
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        ) {
            Text(
                modifier = Modifier.weight(6f),
                text = file.name,
                fontSize = 12.sp,
                color = contentColor,
                textAlign = TextAlign.Left
            )
            Text(
                modifier = Modifier.weight(3f),
                text = json.size.toString(),
                fontSize = 12.sp,
                color = contentColor,
                textAlign = TextAlign.Center
            )
        }

        var buttonHovered by remember { mutableStateOf(false) }
        val density = LocalDensity.current
        var buttonRect by remember { mutableStateOf(IntRect.Zero) }
        val popupShowProgress by animateFloatAsState(
            targetValue = if (buttonHovered) 1f else 0f,
            animationSpec = tween(
                durationMillis = 300,
                delayMillis = 300,
                easing = FastOutSlowInEasing
            )
        )
        GenericButton(
            modifier = Modifier
                .size(HEIGHT * 0.7f)
                .interaction(
                    onHoveredChange = { buttonHovered = it }
                )
                .onGloballyPositioned {
                    buttonRect = it.boundsInWindow().roundToIntRect()
                },
            shadowElevation = 0.dp,
            shape = ROUND_SMALL_CORNER_SHAPE
        ) {
            Text(
                text = "•••",
                color = contentColor,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        AttachedPopup(
            triggerLayoutRect = buttonRect,
            showProgress = popupShowProgress,
            shape = ROUND_SMALL_CORNER_SHAPE,
            minWidth = 0.dp,
            minHeight = 0.dp,
            offset = IntOffset(0, (HEIGHT * 0.7f * density.density).value.roundToInt()),
            align = Alignment.TopEnd
        ) {
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .background(MaterialTheme.colorScheme.surface)
                    .interaction(
                        onHoveredChange = { buttonHovered = it }
                    )
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    PopupContextItem(
                        text = "定位到文件",
                        textColor = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(HEIGHT)
                    )
                    PopupContextItem(
                        text = "删除",
                        textColor = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(HEIGHT)
                    )
                }
            }
        }
    }
}