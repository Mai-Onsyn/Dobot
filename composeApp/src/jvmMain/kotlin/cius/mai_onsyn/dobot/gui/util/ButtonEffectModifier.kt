package cius.mai_onsyn.dobot.gui.util

import androidx.compose.animation.animateColorAsState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon

@Composable
fun Modifier.buttonEffect(
    color: Color = MaterialTheme.colorScheme.scrim,
    hoverAlpha: Float = 0.1f,
    pressedAlpha: Float = 0.2f
): Modifier {
    var isHovered by remember { mutableStateOf(false) }
    var isPressed by remember { mutableStateOf(false) }

    val bgColor by animateColorAsState(
        targetValue =
            if (isPressed) color.copy(pressedAlpha)
            else if (isHovered) color.copy(hoverAlpha)
            else color.copy(0f),
        animationSpec = tweenSpecColor
    )
    return this
        .pointerHoverIcon(PointerIcon.Hand)
        .interaction(
            onHoveredChange = { isHovered = it },
            onPressedChange = { isPressed = it }
        )
        .drawWithContent {
            drawContent()
            drawRect(bgColor)
    }
}