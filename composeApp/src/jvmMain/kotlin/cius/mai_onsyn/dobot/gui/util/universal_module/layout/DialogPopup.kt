package cius.mai_onsyn.dobot.gui.util.universal_module.layout

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import cius.mai_onsyn.dobot.gui.ROUND_CORNER_SHAPE
import cius.mai_onsyn.dobot.gui.util.tweenSpecFloat

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DialogPopup(
    visible: Boolean,
    onDismissRequest: () -> Unit = {},
    shape: Shape = ROUND_CORNER_SHAPE,
    shadowElevation: Dp = 24.dp,
    content: @Composable BoxScope.() -> Unit
) {
    val popupPositionProvider = remember { GlobalPopupPositionProvider() }

    val showProgress by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tweenSpecFloat
    )
    if (showProgress == 0f) return
    Popup(
        popupPositionProvider = popupPositionProvider,
        properties = PopupProperties(focusable = true)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(0.3f * showProgress))
                .pointerInput(Unit) {
                    detectTapGestures { onDismissRequest() }
                }
                .onKeyEvent {
                    if (it.type == KeyEventType.KeyDown && it.key == Key.Escape) {
                        onDismissRequest()
                    }
                    false
                }
        ) {
            Box(
                modifier = Modifier
                    .graphicsLayer {
                        this.shadowElevation = shadowElevation.toPx()
                        alpha = showProgress
                        this.shape = shape
                        val scale = showProgress * 0.2f + 0.8f
                        scaleX = scale
                        scaleY = scale
                        clip = true
                    }
                    .pointerInput(Unit) {
                        detectTapGestures {}
                    }
            ) { content() }
        }
    }
}

class GlobalPopupPositionProvider : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {
        return IntOffset.Zero
    }
}