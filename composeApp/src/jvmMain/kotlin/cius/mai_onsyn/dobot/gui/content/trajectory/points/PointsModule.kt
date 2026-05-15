package cius.mai_onsyn.dobot.gui.content.trajectory.points

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
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
            if (!file!!.exists() || !file!!.isFile) {
                file = null
                return@LaunchedEffect
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
        }



        Text(
            text = "Points Module",
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}