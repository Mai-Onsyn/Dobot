package cius.mai_onsyn.dobot.gui.content.trajectory.file

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cius.mai_onsyn.dobot.gui.GLOBAL_PADDING
import cius.mai_onsyn.dobot.gui.GLOBAL_PADDING_HALF
import cius.mai_onsyn.dobot.gui.ROUND_CORNER_SHAPE
import cius.mai_onsyn.dobot.log

val HEIGHT = 40.dp

@Composable
fun TrajectoryFileTable(
    modifier: Modifier = Modifier,
    trajectoryPaths: List<String>
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .padding(horizontal = GLOBAL_PADDING_HALF)
            .background(MaterialTheme.colorScheme.surface, ROUND_CORNER_SHAPE)
            .verticalScroll(scrollState)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(HEIGHT)
                .fillMaxWidth()
                .padding(horizontal = GLOBAL_PADDING)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
            ) {
                Text(
                    modifier = Modifier.weight(6f),
                    text = "文件名",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Left
                )
                Text(
                    modifier = Modifier.weight(3f),
                    text = "点数量",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
            }

            Text(
                modifier = Modifier,
                text = "操作",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }
        HorizontalDivider(Modifier.fillMaxWidth())

        trajectoryPaths.forEach { item ->
//            log.debug("Selected: ${TrajectoryFileManager.selectedFile}, item: $item")
            key(item) {
                FileItem(
                    selected = item == TrajectoryFileManager.selectedFile,
                    onSelect = { TrajectoryFileManager.selectedFile = it },
                    fileName = item,
                    modifier = Modifier
                        .height(HEIGHT)
                        .fillMaxWidth()
                )
            }
            HorizontalDivider(Modifier.fillMaxWidth())
        }
    }
}