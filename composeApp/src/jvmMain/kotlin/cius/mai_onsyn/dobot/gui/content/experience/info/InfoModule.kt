package cius.mai_onsyn.dobot.gui.content.experience.info

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cius.mai_onsyn.dobot.gui.BLUE_COLOR
import cius.mai_onsyn.dobot.gui.GLOBAL_PADDING
import cius.mai_onsyn.dobot.gui.ROUND_CORNER_SHAPE
import cius.mai_onsyn.dobot.gui.currentStartTime
import cius.mai_onsyn.dobot.gui.experimenting
import cius.mai_onsyn.dobot.gui.util.formatMillisToTime
import cius.mai_onsyn.dobot.gui.util.universal_module.CardBase
import dobot.composeapp.generated.resources.Res
import dobot.composeapp.generated.resources.icon_info
import org.jetbrains.compose.resources.painterResource

@Composable
fun InfoModule(
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    CardBase(
        modifier = modifier
    ) {
        Column {
            Row(
                modifier = Modifier.padding(GLOBAL_PADDING),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(Res.drawable.icon_info),
                    contentDescription = null,
                    tint = colorScheme.onSurface,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "实验信息",
                    color = colorScheme.onSurface,
                    fontSize = 14.sp
                )
            }

            HorizontalDivider()
            Column(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .background(colorScheme.surfaceContainerLow, ROUND_CORNER_SHAPE)
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                InfoItem(
                    modifier = Modifier.height(28.dp),
                    title = "实验文件",
                    content = "细集亚甲蓝.json",
                    contentColor = BLUE_COLOR,
                    extra = {
                        Button(
                            onClick = { experimenting = false },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorScheme.surfaceContainerHigh,
                                contentColor = colorScheme.onSurface
                            ),
                            contentPadding = PaddingValues(0.dp),
                            shape = ROUND_CORNER_SHAPE,
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(56.dp)
                        ) {
                            Text(
                                text = "加载",
                                color = colorScheme.onSurface,
                                fontSize = 13.sp
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                InfoItem(
                    modifier = Modifier.height(28.dp),
                    title = "实验流程",
                    content = "原始文件",
                    extra = {
                        Button(
                            onClick = { experimenting = false },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorScheme.surfaceContainerHigh,
                                contentColor = colorScheme.onSurface
                            ),
                            contentPadding = PaddingValues(0.dp),
                            shape = ROUND_CORNER_SHAPE,
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(56.dp)
                        ) {
                            Text(
                                text = "编辑",
                                color = colorScheme.onSurface,
                                fontSize = 13.sp
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                InfoItem(
                    modifier = Modifier.height(28.dp),
                    title = "沙子重量",
                    content = "200 g",
                    contentColor = BLUE_COLOR
                )
                Spacer(modifier = Modifier.height(8.dp))
                InfoItem(
                    modifier = Modifier.height(28.dp),
                    title = "水体积",
                    content = "500 ml",
                    contentColor = BLUE_COLOR
                )
                Spacer(modifier = Modifier.height(8.dp))
                InfoItem(
                    modifier = Modifier.height(28.dp),
                    title = "当前步骤",
                    content = "抓取物品"
                )
//                var timePassed by remember { mutableStateOf(System.currentTimeMillis() - currentStartTime) }
//                Thread.ofVirtual().start {
//                    Thread.sleep(1000)
//                    if (experimenting) {
//                        timePassed = System.currentTimeMillis() - currentStartTime
//                    } else currentStartTime = System.currentTimeMillis()
//                }
//                Spacer(modifier = Modifier.height(8.dp))
//                InfoItem(
//                    modifier = Modifier.height(28.dp),
//                    title = "预计剩余时间",
//                    content = formatMillisToTime(timePassed),
//                    contentColor = BLUE_COLOR
//                )
            }
        }
    }
}