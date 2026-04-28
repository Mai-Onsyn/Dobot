package cius.mai_onsyn.dobot.gui.util.universal_module

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import cius.mai_onsyn.dobot.gui.util.background

@Composable
fun CardBase(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                baseColor = colorScheme.surfaceContainer,
                hoverColor = colorScheme.surfaceContainerHigh,
                pressedColor = colorScheme.surfaceContainerHigh
            )
    ) {
        Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            content()
        }
    }
}