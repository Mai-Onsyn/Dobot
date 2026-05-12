package cius.mai_onsyn.dobot.gui.util.universal_module

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cius.mai_onsyn.dobot.gui.util.buttonEffect

@Composable
fun GenericButton(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Transparent,
    shape: Shape = RectangleShape,
    borderColor: Color = Color.Transparent,
    borderRadius: Dp = 1.dp,
    shadowElevation: Dp = 4.dp,
    content: @Composable BoxScope.() -> Unit = {}
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .shadow(
                elevation = shadowElevation,
                ambientColor = Color.Black.copy(0.3f),
                shape = shape
            )
            .clip(shape)
            .background(backgroundColor)
            .border(
                width = borderRadius,
                color = borderColor,
                shape = shape
            )
            .buttonEffect()
    ) { content() }
}