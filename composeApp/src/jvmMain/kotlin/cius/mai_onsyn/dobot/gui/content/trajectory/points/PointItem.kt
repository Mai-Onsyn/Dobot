package cius.mai_onsyn.dobot.gui.content.trajectory.points

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.isCtrlPressed
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.roundToIntRect
import androidx.compose.ui.unit.sp
import cius.mai_onsyn.dobot.core.UIInterface.api
import cius.mai_onsyn.dobot.core.trajectory.JointTrajectory
import cius.mai_onsyn.dobot.gui.ROUND_SMALL_CORNER_SHAPE
import cius.mai_onsyn.dobot.gui.content.trajectory.file.HEIGHT
import cius.mai_onsyn.dobot.gui.content.trajectory.points.TrajectoryPointsManager.selectedPointIds
import cius.mai_onsyn.dobot.gui.content.trajectory.points.TrajectoryPointsManager.workingTrajectory
import cius.mai_onsyn.dobot.gui.util.PointColorGenerator
import cius.mai_onsyn.dobot.gui.util.buttonEffect
import cius.mai_onsyn.dobot.gui.util.interaction
import cius.mai_onsyn.dobot.gui.util.tweenSpecColor
import cius.mai_onsyn.dobot.gui.util.universal_module.GenericButton
import cius.mai_onsyn.dobot.gui.util.universal_module.PopupContextItem
import cius.mai_onsyn.dobot.gui.util.universal_module.layout.AttachedPopup
import cius.mai_onsyn.dobot.gui.util.universal_module.layout.ContextMenu
import cius.mai_onsyn.dobot.log
import dobot.composeapp.generated.resources.Res
import dobot.composeapp.generated.resources.icon_list
import org.jetbrains.compose.resources.painterResource
import sh.calvin.reorderable.ReorderableCollectionItemScope
import java.awt.Toolkit
import kotlin.floatArrayOf
import kotlin.math.roundToInt

@Composable
fun ReorderableCollectionItemScope.PointItem(
    modifier: Modifier = Modifier,
    point: PointUiModel,
    index: Int,
    selected: Boolean = false
) {
    val bg by animateColorAsState(
        if (selected) MaterialTheme.colorScheme.primary.copy(0.5f) else Color.Transparent,
        tweenSpecColor
    )
    val content by animateColorAsState(
        if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
        tweenSpecColor
    )
    var hovered by remember { mutableStateOf(false) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(bg)
            .buttonEffect()
            .interaction(
                onClick = {
                    if (!it.keyboardModifiers.isCtrlPressed) {
                        selectedPointIds.clear()
                    }
                    selectedPointIds.add(point.id)
                },
                onHoveredChange = { hovered = it }
            )
    ) {
        val startElementModifier = Modifier
            .width(HEIGHT)
            .fillMaxHeight()
            .background(calcColor(point.point))
        val hapticFeedback = LocalHapticFeedback.current
        if (!hovered) BorderedText(
            isStart = true,
            text = (index + 1).toString(),
            textColor = Color.Black,
            modifier = startElementModifier
        ) else BorderedBox(
            isStart = true,
            modifier = startElementModifier
                .draggableHandle(
                    onDragStarted = {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.GestureThresholdActivate)
                    },
                    onDragStopped = {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.GestureEnd)
                    }
                )
        ) {
            Icon(
                painter = painterResource(Res.drawable.icon_list),
                tint = Color.Black,
                modifier = Modifier.size(20.dp).align(Alignment.Center),
                contentDescription = null
            )
        }
        val p = point.point
        listOf(
            p.joint.j1, p.joint.j2, p.joint.j3, p.joint.j4, p.joint.j5, p.joint.j6,
            p.hand.thumbPitch, p.hand.thumbYaw,
            p.hand.finger1, p.hand.finger2, p.hand.finger3, p.hand.finger4,
            p.hand.thumbRoll
        ).forEach { data ->
            BorderedText(
                text = data.toString(),
                modifier = Modifier.weight(1f).fillMaxHeight(),
                textColor = content
            )
        }

        var buttonHovered by remember { mutableStateOf(false) }
        var buttonRect by remember { mutableStateOf(IntRect.Zero) }
        BorderedBox(
            modifier = Modifier
                .width(HEIGHT)
                .fillMaxHeight()
        ) {
            GenericButton(
                modifier = Modifier
                    .align(Alignment.Center)
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
                    color = content,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
        val density = LocalDensity.current
        ContextMenu(
            anchorRect = buttonRect,
            offset = IntOffset(0, (HEIGHT * 0.7f * density.density).value.roundToInt()),
            align = Alignment.TopEnd,
            show = buttonHovered,
            onHoveredChange = { buttonHovered = it },
            contexts = listOf(
                {
                    PopupContextItem(
                        text = "移动到该点",
                        textColor = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(HEIGHT)
                            .interaction(
                                onClick = {
                                    try {
                                        JointTrajectory.moveTo(point.point, api.robotApi.move, api.robotApi.hand)
                                    } catch (e: Exception) {
                                        log.error("移动到该点失败", e)
                                        Toolkit.getDefaultToolkit().beep()
                                    }
                                }
                            )
                    )
                },
                {
                    PopupContextItem(
                        text = "删除",
                        textColor = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(HEIGHT)
                            .interaction(
                                onClick = {
                                    workingTrajectory.remove(point)
                                }
                            )
                    )
                }
            )
        )
    }
}

private fun calcColor(p: JointTrajectory.Point): Color {
    return Color(PointColorGenerator().generateColor(floatArrayOf(
        p.joint.j1.m(), p.joint.j2.m(), p.joint.j3.m(), p.joint.j4.m(), p.joint.j5.m(), p.joint.j6.m(),
        p.hand.thumbPitch.m(), p.hand.thumbYaw.m(),
        p.hand.finger1.m(), p.hand.finger2.m(), p.hand.finger3.m(), p.hand.finger4.m(),
        p.hand.thumbRoll.m()
    )))
}

private fun Double.m(): Float {
    return (this.toFloat() + 180f) / 360f
}

private fun Int.m(): Float {
    return this.toFloat() / 255f
}