package cius.mai_onsyn.dobot.gui.content.trajectory.points

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cius.mai_onsyn.dobot.core.trajectory.JointTrajectory
import cius.mai_onsyn.dobot.gui.content.trajectory.file.HEIGHT
import cius.mai_onsyn.dobot.gui.content.trajectory.points.TrajectoryPointsManager.selectedPoint
import cius.mai_onsyn.dobot.gui.content.trajectory.points.TrajectoryPointsManager.selectedPoints
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState
import kotlin.random.Random
import kotlin.random.nextInt

private val tableIdentifiers = listOf(
    "J1", "J2", "J3", "J4", "J5", "J6",
    "Tp", "Ty", "F2", "F3", "F4", "F5", "Tr"
)

@Composable
fun PointTable(
    modifier: Modifier = Modifier,
    trajectory: MutableList<JointTrajectory.Point>
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
            items(trajectory, key = { it }) {
                ReorderableItem(reorderableLazyListState, key = it) { isDragging ->
                    val elevation by animateDpAsState(if (isDragging) 4.dp else 0.dp)

                    PointItem(
                        point = it,
                        index = trajectory.indexOf(it),
                        selected = selectedPoints.contains(it),
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