package cius.mai_onsyn.dobot.gui.util.universal_module.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import kotlin.math.roundToInt

@Composable
fun AttachedPopup(
    triggerLayoutRect: IntRect,
    maxWidth: Dp = Dp.Unspecified,
    maxHeight: Dp = Dp.Unspecified,
    minWidth: Dp = Dp.Unspecified,
    minHeight: Dp = Dp.Unspecified,
    followCursor: Boolean = false,
    cursorOffset: IntOffset = IntOffset.Zero,
    cursorPos: IntOffset = IntOffset.Zero,
    showProgress: Float = 1f,
    offset: IntOffset = IntOffset.Zero,
    align: Alignment = Alignment.TopStart,
    elevation: Dp = 8.dp,
    shape: Shape = RoundedCornerShape(8.dp),
    boundCheck: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    val finalOffset = if (followCursor) offset + cursorOffset + cursorPos else offset

    val popupPositionProvider = remember(finalOffset, align, triggerLayoutRect) {
        AttachedPopupPositionProvider(
            offset = finalOffset,
            align = align,
            otherAnchor = triggerLayoutRect,
            boundCheck = boundCheck
        )
    }

    if (showProgress != 0f) Popup(popupPositionProvider = popupPositionProvider) {
        Box(
            modifier = Modifier
                .graphicsLayer {
                    alpha = showProgress
                    shadowElevation = elevation.toPx()
                    this.shape = shape
                    clip = true
                }
                .animatedPopupSize(
                    showProgress = showProgress,
                    minWidth = minWidth,
                    minHeight = minHeight,
                    maxWidth = maxWidth,
                    maxHeight = maxHeight
                )
        ) { content() }
    }
}

fun Modifier.animatedPopupSize(
    showProgress: Float,
    minWidth: Dp,
    minHeight: Dp,
    maxWidth: Dp,
    maxHeight: Dp
) = layout { measurable, constraints ->
    val maxW = if (maxWidth.isSpecified) maxWidth.roundToPx() else constraints.maxWidth
    val maxH = if (maxHeight.isSpecified) maxHeight.roundToPx() else constraints.maxHeight

    val placeable = measurable.measure(
        constraints.copy(
            minWidth = 0,
            minHeight = 0,
            maxWidth = maxW,
            maxHeight = maxH
        )
    )

    val targetW = if (maxWidth.isSpecified) maxW else placeable.width
    val targetH = if (maxHeight.isSpecified) maxH else placeable.height

    val minW = if (minWidth.isSpecified) minWidth.roundToPx() else targetW
    val minH = if (minHeight.isSpecified) minHeight.roundToPx() else targetH

    val currentW = minW + ((targetW - minW) * showProgress).roundToInt()
    val currentH = minH + ((targetH - minH) * showProgress).roundToInt()

    layout(currentW, currentH) {
        placeable.placeRelative(0, 0)
    }
}

class AttachedPopupPositionProvider(
    val offset: IntOffset = IntOffset.Zero,
    val align: Alignment = Alignment.TopStart,
    val boundCheck: Boolean = true,
    val otherAnchor: IntRect? = null
) : PopupPositionProvider {

    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {
        val actualAnchor = otherAnchor ?: anchorBounds

        val alignmentOffset = align.align(
            size = popupContentSize,
            space = IntSize(actualAnchor.width, actualAnchor.height),
            layoutDirection = layoutDirection
        )

        var x = actualAnchor.left + alignmentOffset.x + offset.x
        var y = actualAnchor.top + alignmentOffset.y + offset.y

        if (boundCheck) {
            x = x.coerceIn(0, (windowSize.width - popupContentSize.width).coerceAtLeast(0))
            y = y.coerceIn(0, (windowSize.height - popupContentSize.height).coerceAtLeast(0))
        }

        return IntOffset(x, y)
    }
}