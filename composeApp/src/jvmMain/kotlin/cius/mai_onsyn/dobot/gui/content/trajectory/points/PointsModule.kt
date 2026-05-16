package cius.mai_onsyn.dobot.gui.content.trajectory.points

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import cius.mai_onsyn.dobot.core.trajectory.JointTrajectory
import cius.mai_onsyn.dobot.gui.GLOBAL_PADDING
import cius.mai_onsyn.dobot.gui.content.trajectory.file.TrajectoryFileManager
import cius.mai_onsyn.dobot.gui.content.trajectory.points.TrajectoryPointsManager.file
import cius.mai_onsyn.dobot.gui.util.universal_module.layout.CardBase
import java.io.File

@Composable
fun PointsModule(
    modifier: Modifier = Modifier
) {
    CardBase(modifier = modifier) {
        LaunchedEffect(TrajectoryFileManager.selectedFile) {
            file = File("${TrajectoryFileManager.workingDir}/${TrajectoryFileManager.selectedFile}")
            if (!file!!.exists() || !file!!.isFile || file!!.extension != "json") {
                file = null
            }
        }
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "轨迹点列表${file?.let { ":   ${it.name}" } ?: ""}",
                style = TextStyle(fontWeight = FontWeight.Bold),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(GLOBAL_PADDING)
            )
            if (file != null) {
                val trajectory = remember { mutableStateListOf<JointTrajectory.Point>() }
                LaunchedEffect(file) {
                    val loaded = JointTrajectory.load(file!!.absolutePath)
                    trajectory.clear()
                    trajectory.addAll(loaded)
                }
                PointTable(
                    trajectory = trajectory,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
            }

            Spacer(Modifier.height(GLOBAL_PADDING))
        }
        if (file == null) {
            Text(
                text = "选择并打开一个文件",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}