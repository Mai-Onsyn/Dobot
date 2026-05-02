package cius.mai_onsyn.dobot.gui.content.experience.log

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cius.mai_onsyn.dobot.gui.ROUND_CORNER_RADIUS
import cius.mai_onsyn.dobot.gui.ROUND_CORNER_SHAPE
import cius.mai_onsyn.dobot.gui.content.experience.log.realtime_data.MonitorData
import cius.mai_onsyn.dobot.gui.content.experience.log.realtime_data.MonitoringDashboard
import cius.mai_onsyn.dobot.gui.util.tweenSpecFloat
import cius.mai_onsyn.dobot.gui.util.universal_module.CardBase
import cius.mai_onsyn.dobot.log
import dobot.composeapp.generated.resources.Res
import dobot.composeapp.generated.resources.icon_dustbin
import dobot.composeapp.generated.resources.icon_export
import dobot.composeapp.generated.resources.icon_log
import dobot.composeapp.generated.resources.icon_readtime_data
import org.apache.logging.log4j.core.LogEvent
import org.jetbrains.compose.resources.painterResource

private var tabIndex by mutableStateOf(0)
val logs = mutableStateListOf<LogEvent>()

@Composable
fun LogModule(
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    CardBase(
        modifier = modifier
    ) {
        Column {
            Box(
                modifier = Modifier
                    .height(48.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 7.dp)
            ) {
                PrimaryTabRow(
                    selectedTabIndex = tabIndex,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .width(240.dp),
                    containerColor = Color.Transparent,
                    contentColor = TabRowDefaults.primaryContentColor,
                    indicator = {
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier
                                .tabIndicatorOffset(tabIndex, matchContentSize = false)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(4.dp)),
                            height = 3.dp,
                            color = colorScheme.secondary
                        )
                    },
                    divider = {}
                ) {
                    Tab(
                        modifier = Modifier
                            .height(42.dp)
                            .clip(RoundedCornerShape(topStart = ROUND_CORNER_RADIUS, topEnd = ROUND_CORNER_RADIUS)),
                        selected = tabIndex == 0,
                        onClick = { tabIndex = 0 },
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(Res.drawable.icon_log),
                                    contentDescription = null,
                                    tint = colorScheme.onSurface,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "日志输出",
                                    color = colorScheme.onSurface,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    )
                    Tab(
                        modifier = Modifier
                            .height(42.dp)
                            .clip(RoundedCornerShape(topStart = ROUND_CORNER_RADIUS, topEnd = ROUND_CORNER_RADIUS)),
                        selected = tabIndex == 1,
                        onClick = { tabIndex = 1 },
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(Res.drawable.icon_readtime_data),
                                    contentDescription = null,
                                    tint = colorScheme.onSurface,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "实时数据",
                                    color = colorScheme.onSurface,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    )
                }

                AnimatedContent(
                    targetState = tabIndex,
                    transitionSpec = {
                        fadeIn(animationSpec = tweenSpecFloat) + slideIn(
                            initialOffset = {
                                IntOffset(if (tabIndex != 0) it.width / 4 else -it.width / 4, 0)
                            }
                        ) togetherWith
                                fadeOut(animationSpec = tweenSpecFloat)
                    },
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .fillMaxHeight()
                        .padding(bottom = 7.dp)
                ) {
                    if (tabIndex == 0) Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        SearchInput(
                            modifier = Modifier
                                .width(180.dp)
                                .fillMaxHeight()
                        )
                        Spacer(modifier = Modifier.padding(4.dp))
                        ButtonWithIcon(
                            icon = Res.drawable.icon_dustbin,
                            text = "清空",
                            modifier = Modifier
                                .width(64.dp)
                                .fillMaxHeight()
                        )
                        Spacer(modifier = Modifier.padding(4.dp))
                        ButtonWithIcon(
                            icon = Res.drawable.icon_export,
                            text = "导出",
                            modifier = Modifier
                                .width(64.dp)
                                .fillMaxHeight()
                        )
                    }
                }

            }
//            Spacer(modifier = Modifier.height(4.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(4.dp))

            AnimatedContent(
                targetState = tabIndex,
                transitionSpec = {
                    fadeIn(animationSpec = tweenSpecFloat) + slideIn(
                        initialOffset = {
                            IntOffset(if (tabIndex != 0) it.width / 4 else -it.width / 4, 0)
                        }
                    ) togetherWith
                            fadeOut(animationSpec = tweenSpecFloat)
                }
            ) {
                when (it) {
                    0 -> Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp)
                            .clip(ROUND_CORNER_SHAPE)
                            .background(MaterialTheme.colorScheme.surfaceContainerLow)
                    ) {

                        LaunchedEffect(Unit) {
//                            log.info("test message")
//                            log.error("test message2")
//                            log.warn("test message3")
                            for (i in LOG_QUEUE) {
                                logs.add(i)
                            }
                        }
                        val state = rememberLazyListState()
                        LaunchedEffect(logs.size) {
                            if (logs.isNotEmpty()) {
                                state.animateScrollToItem(logs.lastIndex)
                            }
                        }
                        LazyColumn(
                            modifier = Modifier
                                .padding(12.dp)
                                .fillMaxSize(),
                            state = state,
                        ) {
                            items(logs) { log ->
                                LogItem(
                                    event = log,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                )
                                if (logs.indexOf(log) != logs.lastIndex) {
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Spacer(modifier = Modifier.height(2.dp))
                                }
                            }
                        }
                    }
                    1 -> MonitoringDashboard(
                        MonitorData(
                            soilContent = 63.4f,
                            trajectoryCount = 12,
                            loopCount = 4,
                            runningTime = "6分钟",
                            colorIndex = 78f,
                            confidence = 0.9284f,
                            robotPos = Triple(132.3f, -0.2f, 32.1f),
                            jointAngles = listOf(179.8f,32.5f, 25.9f, -30.6f, 104.4f, 0f),
                            mixerSpeed = 400,
                            elevatorStatus = "READY"
                        )
                    )
                }
            }
        }
    }
}