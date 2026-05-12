package cius.mai_onsyn.dobot.gui.content.trajectory.file

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cius.mai_onsyn.dobot.gui.GLOBAL_PADDING
import cius.mai_onsyn.dobot.gui.GLOBAL_PADDING_HALF
import cius.mai_onsyn.dobot.gui.ROUND_CORNER_SHAPE
import cius.mai_onsyn.dobot.gui.util.universal_module.ButtonWithIcon
import cius.mai_onsyn.dobot.gui.util.universal_module.GenericButton
import cius.mai_onsyn.dobot.gui.util.universal_module.layout.CardBase
import cius.mai_onsyn.dobot.gui.util.universal_module.SearchInput
import dobot.composeapp.generated.resources.Res
import dobot.composeapp.generated.resources.icon_reset
import java.io.File

@Composable
fun FileModule(
    modifier: Modifier = Modifier
) {
    CardBase(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = GLOBAL_PADDING),
        ) {
            Text(
                text = "轨迹文件管理",
                style = TextStyle(fontWeight = FontWeight.Bold),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = GLOBAL_PADDING)
            )
            Spacer(Modifier.height(GLOBAL_PADDING))
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = GLOBAL_PADDING)
            ) {
                SearchInput(
                    prompt = "搜索文件",
                    modifier = Modifier
                        .height(28.dp)
                        .weight(1f)
                )
                Spacer(Modifier.width(GLOBAL_PADDING_HALF))
                ButtonWithIcon(
                    icon = Res.drawable.icon_reset,
                    modifier = Modifier.size(28.dp, 28.dp),
                )
                Spacer(Modifier.width(GLOBAL_PADDING_HALF))
                ButtonWithIcon(
                    icon = Res.drawable.icon_reset,
                    modifier = Modifier.size(28.dp, 28.dp),
                )
            }
            Spacer(Modifier.height(GLOBAL_PADDING))

            TrajectoryFileTable(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                trajectoryPaths = TrajectoryFileLoader.files
            )

//            Spacer(Modifier.height(GLOBAL_PADDING))

            Row(
                modifier = Modifier
                    .padding(GLOBAL_PADDING)
            ) {
                GenericButton(
                    backgroundColor = MaterialTheme.colorScheme.secondary,
                    shape = ROUND_CORNER_SHAPE,
                    modifier = Modifier
                        .height(56.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = "新建",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSecondary,
                    )
                }
                Spacer(Modifier.width(GLOBAL_PADDING))
                GenericButton(
                    backgroundColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    shape = ROUND_CORNER_SHAPE,
                    modifier = Modifier
                        .height(56.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = "导入",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }
    }
}

object TrajectoryFileLoader {
    val files = mutableStateListOf<String>()
    var selectedFile by mutableStateOf("")

    init { update() }

    fun update() {
        val folder = File("traj2/")
        if (folder.exists() && folder.isDirectory) {
            files.clear()
            folder.listFiles()?.forEach { file ->
                if (file.isFile && file.extension == "json") {
                    files.add(file.absolutePath)
                }
            }
        }
    }
}