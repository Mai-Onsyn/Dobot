package cius.mai_onsyn.dobot.gui.content.trajectory.points

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cius.mai_onsyn.dobot.gui.content.trajectory.file.HEIGHT
import cius.mai_onsyn.dobot.gui.content.trajectory.points.TrajectoryPointsManager.selectedPointIds
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

private val tableIdentifiers = listOf(
    "J1", "J2", "J3", "J4", "J5", "J6",
    "Tp", "Ty", "F2", "F3", "F4", "F5", "Tr"
)

@Composable
fun PointTable(
    modifier: Modifier = Modifier,
    trajectory: MutableList<PointUiModel>
) {
    Column(
        modifier = modifier
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(HEIGHT * 1.2f)
        ) {
            BorderedText(
                isStart = true,
                text = "序号",
                textStyle = TextStyle(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .width(HEIGHT)
                    .fillMaxHeight()
            )
            repeat(13) {
                BorderedText(
                    text = tableIdentifiers[it],
                    textStyle = TextStyle(fontWeight = FontWeight.Bold),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
            }
            BorderedText(
                text = "操作",
                textStyle = TextStyle(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .width(HEIGHT)
                    .fillMaxHeight()
            )
        }

        val hapticFeedback = LocalHapticFeedback.current

        val lazyListState = rememberLazyListState()
        val reorderableLazyListState = rememberReorderableLazyListState(lazyListState) { from, to ->
            trajectory.apply { add(to.index, removeAt(from.index)) }
            hapticFeedback.performHapticFeedback(HapticFeedbackType.SegmentFrequentTick)
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = lazyListState
        ) {
            itemsIndexed(trajectory, key = { _, item -> item.id }) { index, item ->
                ReorderableItem(reorderableLazyListState, key = item.id) { isDragging ->
                    val elevation by animateDpAsState(if (isDragging) 4.dp else 0.dp)

                    PointItem(
                        point = item,
                        index = index,
                        selected = selectedPointIds.contains(item.id),
                        modifier = Modifier
                            .shadow(elevation = elevation)
                            .fillMaxWidth()
                            .height(HEIGHT)
                    )
                }
            }
        }
    }
}