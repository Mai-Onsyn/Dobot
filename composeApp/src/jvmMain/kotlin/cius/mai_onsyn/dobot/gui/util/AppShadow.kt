package cius.mai_onsyn.dobot.gui.util

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import cius.mai_onsyn.dobot.gui.ROUND_CORNER_SHAPE

@Composable
fun Modifier.appShadow(
    shadowShape: Shape = ROUND_CORNER_SHAPE,
    opacity: Float = 1.0f,
    doClip: Boolean = false
): Modifier {
    val theme = MaterialTheme.colorScheme
    return this
        .graphicsLayer {
            alpha = opacity
            shadowElevation = 12.dp.toPx()
            shape = shadowShape
            clip = doClip
            ambientShadowColor = theme.inverseSurface.copy(0.5f)
            spotShadowColor = theme.inverseSurface.copy(0.3f)
        }
}