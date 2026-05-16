package cius.mai_onsyn.dobot.gui.content.trajectory.replay

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cius.mai_onsyn.dobot.core.UIInterface.api
import cius.mai_onsyn.dobot.core.robot.hand.HandJoint
import cius.mai_onsyn.dobot.gui.*
import cius.mai_onsyn.dobot.gui.content.experience.control.MorphingPlayPauseButton
import cius.mai_onsyn.dobot.gui.content.trajectory.points.TrajectoryPointsManager.selectedPointIds
import cius.mai_onsyn.dobot.gui.content.trajectory.points.TrajectoryPointsManager.workingTrajectory
import cius.mai_onsyn.dobot.gui.util.interaction
import cius.mai_onsyn.dobot.gui.util.tweenSpecColor
import cius.mai_onsyn.dobot.gui.util.universal_module.GenericButton
import cius.mai_onsyn.dobot.gui.util.universal_module.TextField
import cius.mai_onsyn.dobot.gui.util.universal_module.layout.CardBase
import cius.mai_onsyn.dobot.log
import java.awt.Toolkit

var isPlaying by mutableStateOf(false)
var isThreadRunning = false

@Composable
fun ReplayModule(
    modifier: Modifier = Modifier
) {
    CardBase(modifier = modifier) {
        Column(
            modifier = modifier.fillMaxSize().padding(GLOBAL_PADDING)
        ) {
            Text(
                text = "回放控制",
                style = TextStyle(fontWeight = FontWeight.Bold),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(Modifier.height(GLOBAL_PADDING))

            Box(Modifier.fillMaxSize()) {
                Column(
                    Modifier.align(Alignment.Center)
                ) {
                    var text by remember { mutableStateOf("0") }
                    LaunchedEffect(selectedPointIds.toList()) {
                        if (selectedPointIds.size == 1) {
                            text = (workingTrajectory.indexOfFirst { selectedPointIds.first() == it.id } + 1).toString()
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "起始点",
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Spacer(Modifier.width(GLOBAL_PADDING_HALF))
                        TextField(
                            value = text,
                            onValueChange = { text = it },
                            shape = ROUND_SMALL_CORNER_SHAPE,
                            maxLines = 1,
                            borderColor = MaterialTheme.colorScheme.outline.copy(0.2f),
                            backgroundColor = MaterialTheme.colorScheme.surface,
                            modifier = Modifier.size(48.dp, 36.dp)
                        )
                        Spacer(Modifier.width(GLOBAL_PADDING_HALF))

                        GenericButton(
                            modifier = Modifier.size(36.dp).interaction(onClick = {
                                val index = text.toIntOrNull() ?: 1
                                try {
                                    text = (index + 1).coerceIn(1..workingTrajectory.size).toString()
                                } catch (e: Exception) {}
                            }),
                            shadowElevation = 0.dp,
                            shape = CircleShape
                        ) {
                            Text(
                                text = "+",
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                        GenericButton(
                            modifier = Modifier.size(36.dp).interaction(onClick = {
                                val index = text.toIntOrNull() ?: 1
                                try {
                                    text = (index - 1).coerceIn(1..workingTrajectory.size).toString()
                                } catch (e: Exception) {}
                            }),
                            shadowElevation = 0.dp,
                            shape = CircleShape
                        ) {
                            Text(
                                text = "-",
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    }
                    Spacer(Modifier.height(GLOBAL_PADDING_HALF))
                    HorizontalDivider(Modifier.width(220.dp))
                    Spacer(Modifier.height(GLOBAL_PADDING_HALF))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        val bg by animateColorAsState(
                            if (isPlaying) RED_COLOR else BLUE_COLOR,
                            tweenSpecColor
                        )
                        GenericButton(
                            shape = ROUND_CORNER_SHAPE,
                            shadowElevation = 4.dp,
                            backgroundColor = bg,
                            modifier = Modifier
                                .size(128.dp, 64.dp)
                                .interaction(
                                    onClick = {
                                        if (!isThreadRunning) isPlaying = !isPlaying
                                        if (isPlaying) {
                                            Thread.ofVirtual().start {
                                                val startIndex = text.toIntOrNull()
                                                if (startIndex == null) {
                                                    isPlaying = false
                                                    return@start
                                                }
                                                isThreadRunning = true

                                                val tr = workingTrajectory.subList(startIndex, workingTrajectory.size).map { it.point }
                                                var i = 0
                                                var lastHand: HandJoint? = null
                                                while (isPlaying && i < tr.size) {
                                                    try {
                                                        tr[i].moveTo(api.robotApi.move, api.robotApi.hand, block = true)
                                                        if (lastHand != null && lastHand != tr[i].hand) {
                                                            Thread.sleep(2000)
                                                        }
                                                        lastHand = tr[i++].hand
                                                    } catch (e: Exception) {
                                                        log.error("Error while replaying trajectory", e)
                                                        Toolkit.getDefaultToolkit().beep()
                                                        break
                                                    }
//                                                    Thread.sleep(1000)
//                                                    text = (text.toInt() + 1).toString()
//                                                    i++
                                                }
                                                isPlaying = false
                                                isThreadRunning = false
                                            }
                                        }
                                    }
                                )
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                MorphingPlayPauseButton(
                                    isPlaying = isPlaying,
                                    modifier = Modifier.size(28.dp),
                                    onToggle = {  },
                                    fill = WHITE_COLOR
                                )
                                Spacer(Modifier.width(GLOBAL_PADDING_HALF))
                                Text(
                                    text = if (isPlaying) "停止" else "开始",
                                    color = WHITE_COLOR,
                                )
                            }
                        }
                        Spacer(Modifier.width(GLOBAL_PADDING * 1.5f))
                        VerticalDivider(Modifier.height(64.dp))
                        Spacer(Modifier.width(GLOBAL_PADDING))
                        Text(
                            text = "${text.toIntOrNull() ?: 0} / ${workingTrajectory.size}",
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
            }
        }
    }
}