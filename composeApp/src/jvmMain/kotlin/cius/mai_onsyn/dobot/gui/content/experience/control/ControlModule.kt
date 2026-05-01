package cius.mai_onsyn.dobot.gui.content.experience.control

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cius.mai_onsyn.dobot.gui.BLUE_COLOR
import cius.mai_onsyn.dobot.gui.GLOBAL_PADDING
import cius.mai_onsyn.dobot.gui.GREEN_COLOR
import cius.mai_onsyn.dobot.gui.RED_COLOR
import cius.mai_onsyn.dobot.gui.ROUND_CORNER_SHAPE
import cius.mai_onsyn.dobot.gui.WHITE_COLOR
import cius.mai_onsyn.dobot.gui.expProgress
import cius.mai_onsyn.dobot.gui.experimenting
import cius.mai_onsyn.dobot.gui.util.background
import cius.mai_onsyn.dobot.gui.util.tweenSpecColor
import cius.mai_onsyn.dobot.gui.util.tweenSpecFloat
import cius.mai_onsyn.dobot.gui.util.universal_module.CardBase
import dobot.composeapp.generated.resources.Res
import dobot.composeapp.generated.resources.icon_experiment
import dobot.composeapp.generated.resources.icon_reset
import org.jetbrains.compose.resources.painterResource

@Composable
fun ControlModule(
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    CardBase(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(GLOBAL_PADDING)
        ) {
            Column(
                modifier = Modifier
                    .width(300.dp)
                    .fillMaxHeight()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.icon_experiment),
                        contentDescription = null,
                        tint = colorScheme.onSurface,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "实验控制",
                        color = colorScheme.onSurface,
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.height(GLOBAL_PADDING))
                Row(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Button(
                        onClick = { experimenting = !experimenting },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = animateColorAsState(
                                if (experimenting) RED_COLOR else GREEN_COLOR,
                                tweenSpecColor
                            ).value,
                            contentColor = WHITE_COLOR
                        ),
                        contentPadding = PaddingValues(0.dp),
                        shape = ROUND_CORNER_SHAPE,
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                color = colorScheme.outline.copy(0.2f),
                                shape = ROUND_CORNER_SHAPE
                            )
                            .fillMaxHeight()
                            .weight(1f)
                    ) {
                        MorphingPlayPauseButton(
                            onToggle = {},
                            fill = WHITE_COLOR,
                            isPlaying = experimenting,
                            modifier = Modifier
                                .size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (experimenting) "停止" else "开始",
                            fontSize = 14.sp,
                            color = WHITE_COLOR
                        )
                    }
                    Spacer(modifier = Modifier.width(GLOBAL_PADDING))
                    Button(
                        onClick = { experimenting = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorScheme.surfaceContainer,
                            contentColor = colorScheme.onSurface
                        ),
                        contentPadding = PaddingValues(0.dp),
                        shape = ROUND_CORNER_SHAPE,
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                color = colorScheme.outline.copy(0.2f),
                                shape = ROUND_CORNER_SHAPE
                            )
                            .fillMaxHeight()
                            .weight(1f)
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.icon_reset),
                            contentDescription = null,
                            tint = colorScheme.onSurface,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "复位",
                            fontSize = 14.sp,
                            color = colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(GLOBAL_PADDING))
            VerticalDivider(
                modifier = Modifier.fillMaxHeight(),
                thickness = 1.dp,
                color = colorScheme.outline.copy(0.2f),
            )
            Spacer(modifier = Modifier.width(GLOBAL_PADDING))

            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Spacer(modifier = Modifier.weight(0.4f))
                Text(
                    text = "实验进度",
                    color = colorScheme.onSurface,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val animatedProgress = animateFloatAsState(
                        targetValue = expProgress,
                        animationSpec = tweenSpecFloat
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(10.dp)
                            .clip(ROUND_CORNER_SHAPE)
                            .background(colorScheme.outline.copy(0.2f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(animatedProgress.value)
                                .fillMaxHeight()
                                .clip(ROUND_CORNER_SHAPE)
                                .background(BLUE_COLOR)
                        )
                    }
                    Spacer(modifier = Modifier.width(GLOBAL_PADDING))
                    Text(
                        text = String.format("%.1f%%", expProgress * 100),
                        color = colorScheme.onSurface,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "已用时间: 00:00",
                        color = colorScheme.onSurface,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "预计剩余: 00:00",
                        color = colorScheme.onSurface,
                        fontSize = 13.sp,
                        modifier = Modifier.align(Alignment.CenterEnd)
                    )
                }
            }
        }
    }
}