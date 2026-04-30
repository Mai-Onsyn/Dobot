package cius.mai_onsyn.dobot.gui.content.experience.log

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Text
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cius.mai_onsyn.dobot.gui.ROUND_CORNER_SHAPE
import cius.mai_onsyn.dobot.gui.util.universal_module.CardBase
import cius.mai_onsyn.dobot.log
import dobot.composeapp.generated.resources.Res
import dobot.composeapp.generated.resources.icon_delete
import dobot.composeapp.generated.resources.icon_export
import org.apache.logging.log4j.core.LogEvent

@Composable
fun LogModule(
    modifier: Modifier = Modifier
) {
    CardBase(
        modifier = modifier
    ) {
        Column {
            Box(
                modifier = Modifier
                    .height(48.dp)
                    .fillMaxWidth()
                    .padding(vertical = 7.dp, horizontal = 16.dp)
            ) {
                Text(
                    text = "日志输出",
                    modifier = Modifier
                        .align(Alignment.CenterStart),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .fillMaxHeight()
                ) {
                    SearchInput(
                        modifier = Modifier
                            .width(180.dp)
                            .fillMaxHeight()
                    )
                    Spacer(modifier = Modifier.padding(4.dp))
                    ButtonWithIcon(
                        icon = Res.drawable.icon_delete,
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
//            Spacer(modifier = Modifier.height(4.dp))
            HorizontalDivider(modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(4.dp))

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp)
                    .clip(ROUND_CORNER_SHAPE)
                    .background(MaterialTheme.colorScheme.surfaceContainerLow)
            ) {
                log.info("test message")
                log.error("test message2")
                log.warn("test message3")

                val logs = remember { mutableStateListOf<LogEvent>() }
                LaunchedEffect(Unit) {
                    for (i in LOG_QUEUE) {
                        logs.add(i)
                    }
                }
                val state = rememberLazyListState()
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
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        }
    }
}