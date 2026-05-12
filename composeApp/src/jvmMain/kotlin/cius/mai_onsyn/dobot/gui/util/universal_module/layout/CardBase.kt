package cius.mai_onsyn.dobot.gui.util.universal_module.layout

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import cius.mai_onsyn.dobot.gui.ROUND_CORNER_SHAPE
import cius.mai_onsyn.dobot.gui.util.background

@Composable
fun CardBase(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    Box(
        modifier = modifier
            .clip(ROUND_CORNER_SHAPE)
            .border(
                width = 0.5.dp,
                color = colorScheme.outline.copy(0.2f),
                shape = ROUND_CORNER_SHAPE
            )
            .background(
                baseColor = colorScheme.surfaceContainer,
                hoverColor = colorScheme.surfaceContainerHigh,
                pressedColor = colorScheme.surfaceContainerHigh
            )
    ) {
        Box(modifier = Modifier.fillMaxSize()/*.padding(4.dp)*/) {
            content()
        }
    }
}