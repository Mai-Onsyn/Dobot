package cius.mai_onsyn.dobot.gui.util.universal_module.layout

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.dp
import cius.mai_onsyn.dobot.gui.ROUND_SMALL_CORNER_SHAPE
import cius.mai_onsyn.dobot.gui.util.interaction

@Composable
fun ContextMenu(
    anchorRect: IntRect,
    offset: IntOffset = IntOffset.Zero,
    align: Alignment = Alignment.TopStart,
    show: Boolean = false,
    onHoveredChange: (Boolean) -> Unit = {},
    contexts: List<@Composable () -> Unit>,
) {
    var copyProgress by remember { mutableStateOf(0f) }
    val popupShowProgress by animateFloatAsState(
        targetValue = if (show) 1f else 0f,
        animationSpec = tween(
            durationMillis = 300,
            delayMillis = if (copyProgress == 0f || copyProgress == 1f) 300 else 0,
            easing = FastOutSlowInEasing
        )
    )
    LaunchedEffect(popupShowProgress) {
        copyProgress = popupShowProgress
    }
    AttachedPopup(
        triggerLayoutRect = anchorRect,
        showProgress = popupShowProgress,
        shape = ROUND_SMALL_CORNER_SHAPE,
        minWidth = 0.dp,
        minHeight = 0.dp,
        offset = offset,
        align = align
    ) {
        Box(
            modifier = Modifier
                .width(120.dp)
                .background(MaterialTheme.colorScheme.surface)
                .interaction(
                    onHoveredChange = onHoveredChange
                )
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                contexts.forEach { item ->
                    item()
                }
            }
        }
    }
}