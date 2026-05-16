package cius.mai_onsyn.dobot.gui.content.trajectory.points

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.isCtrlPressed
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import cius.mai_onsyn.dobot.core.trajectory.JointTrajectory
import cius.mai_onsyn.dobot.gui.content.trajectory.file.HEIGHT
import cius.mai_onsyn.dobot.gui.content.trajectory.points.TrajectoryPointsManager.selectedPoint
import cius.mai_onsyn.dobot.gui.content.trajectory.points.TrajectoryPointsManager.selectedPoints
import cius.mai_onsyn.dobot.gui.util.ColorMapper
import cius.mai_onsyn.dobot.gui.util.PointColorGenerator
import cius.mai_onsyn.dobot.gui.util.interaction
import cius.mai_onsyn.dobot.gui.util.tweenSpecColor
import dobot.composeapp.generated.resources.Res
import dobot.composeapp.generated.resources.icon_list
import org.jetbrains.compose.resources.painterResource
import sh.calvin.reorderable.ReorderableCollectionItemScope
import kotlin.floatArrayOf

@Composable
fun ReorderableCollectionItemScope.PointItem(
    modifier: Modifier = Modifier,
    point: JointTrajectory.Point,
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
            .interaction(
                onClick = {
//                    selectedPoint = point
                    if (!it.keyboardModifiers.isCtrlPressed) {
                        selectedPoints.clear()
                    }
                    selectedPoints.add(point)
                },
                onHoveredChange = { hovered = it }
            )
    ) {
        val startElementModifier = Modifier
            .width(HEIGHT)
            .fillMaxHeight()
            .background(calcColor(point))
        val hapticFeedback = LocalHapticFeedback.current
        if (!hovered) BorderedText(
            isStart = true,
            text = index.toString(),
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
                tint = content,
                modifier = Modifier.size(20.dp).align(Alignment.Center),
                contentDescription = null
            )
        }
        listOf(
            point.joint.j1, point.joint.j2, point.joint.j3, point.joint.j4, point.joint.j5, point.joint.j6,
            point.hand.thumbPitch, point.hand.thumbYaw,
            point.hand.finger1, point.hand.finger2, point.hand.finger3, point.hand.finger4,
            point.hand.thumbRoll
        ).forEach { data ->
            BorderedText(
                text = data.toString(),
                modifier = Modifier.weight(1f).fillMaxHeight(),
                textColor = content
            )
        }

        BorderedText(
            textColor = content,
            text = "•••",
            modifier = Modifier
                .width(HEIGHT)
                .fillMaxHeight()
        )
    }
}

private fun calcColor(p: JointTrajectory.Point): Color {
//    val (r, g, b) = ColorMapper().pointToRgb()
    return Color(PointColorGenerator().generateColor(floatArrayOf(
        p.joint.j1.m(), p.joint.j2.m(), p.joint.j3.m(), p.joint.j4.m(), p.joint.j5.m(), p.joint.j6.m(),
        p.hand.thumbPitch.m(), p.hand.thumbYaw.m(),
        p.hand.finger1.m(), p.hand.finger2.m(), p.hand.finger3.m(), p.hand.finger4.m(),
        p.hand.thumbRoll.m()
    )))
//    return Color(r, g, b, 1f)
}

private fun Double.m(): Float {
    return (this.toFloat() + 180f) / 360f
}

private fun Int.m(): Float {
    return this.toFloat() / 255f
}