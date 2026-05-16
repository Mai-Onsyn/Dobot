package cius.mai_onsyn.dobot.gui.content.trajectory.file

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cius.mai_onsyn.dobot.core.trajectory.JointTrajectory
import cius.mai_onsyn.dobot.gui.GLOBAL_PADDING
import cius.mai_onsyn.dobot.gui.GLOBAL_PADDING_HALF
import cius.mai_onsyn.dobot.gui.ROUND_CORNER_SHAPE
import cius.mai_onsyn.dobot.gui.content.trajectory.file.TrajectoryFileManager.workingDir
import cius.mai_onsyn.dobot.gui.content.trajectory.points.TrajectoryPointsManager
import cius.mai_onsyn.dobot.gui.util.interaction
import cius.mai_onsyn.dobot.gui.util.universal_module.ButtonWithIcon
import cius.mai_onsyn.dobot.gui.util.universal_module.GenericButton
import cius.mai_onsyn.dobot.gui.util.universal_module.SearchInput
import cius.mai_onsyn.dobot.gui.util.universal_module.layout.CardBase
import cius.mai_onsyn.dobot.gui.util.universal_module.layout.SingleTextInputDialog
import dobot.composeapp.generated.resources.Res
import dobot.composeapp.generated.resources.icon_log
import dobot.composeapp.generated.resources.icon_reset
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.absolutePath
import io.github.vinceglb.filekit.dialogs.openDirectoryPicker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
                    modifier = Modifier.size(28.dp, 28.dp)
                        .interaction(
                            onClick = { TrajectoryFileManager.update() }
                        )
                )
                Spacer(Modifier.width(GLOBAL_PADDING_HALF))
                ButtonWithIcon(
                    icon = Res.drawable.icon_log,
                    modifier = Modifier.size(28.dp, 28.dp)
                        .interaction(
                            onClick = {
                                CoroutineScope(Dispatchers.IO).launch {
                                    val folder = FileKit.openDirectoryPicker(PlatformFile(workingDir)) ?: return@launch
                                    workingDir = folder.absolutePath()
                                    TrajectoryFileManager.update()
                                }
                            }
                        )
                )
            }
            Spacer(Modifier.height(GLOBAL_PADDING))

            TrajectoryFileTable(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                trajectoryPaths = TrajectoryFileManager.files
            )

//            Spacer(Modifier.height(GLOBAL_PADDING))

            Row(
                modifier = Modifier
                    .padding(GLOBAL_PADDING)
            ) {
                var showNewDialog by remember { mutableStateOf(false) }
                GenericButton(
                    backgroundColor = MaterialTheme.colorScheme.secondary,
                    shape = ROUND_CORNER_SHAPE,
                    modifier = Modifier
                        .height(56.dp)
                        .weight(1f)
                        .interaction(
                            onClick = { showNewDialog = true },
                        )
                ) {
                    Text(
                        text = "新建",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSecondary,
                    )
                    SingleTextInputDialog(
                        visible = showNewDialog,
                        title = "新建轨迹文件",
                        placeholder = "轨迹名称",
                        confirmText = "创建",
                        suffix = ".json",
                        onDismissRequest = { showNewDialog = false },
                        validator = { if (it.isEmpty()) "文件名不能为空" else null }
                    ) { filename ->
                        TrajectoryFileManager.create(filename)
                        TrajectoryFileManager.selectedFile = "$filename.json"
                        showNewDialog = false
                    }
                }
                Spacer(Modifier.width(GLOBAL_PADDING))
                GenericButton(
                    backgroundColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    shape = ROUND_CORNER_SHAPE,
                    modifier = Modifier
                        .height(56.dp)
                        .weight(1f)
                        .interaction(
                            onClick = {
                                val tr = JointTrajectory()
                                TrajectoryPointsManager.workingTrajectory.forEach {
                                    tr.add(it.point)
                                }
                                tr.write("$workingDir/${TrajectoryFileManager.selectedFile}")
                            }
                        )
                ) {
                    Text(
                        text = "保存",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }

            Text(
                text = workingDir,
                modifier = Modifier.padding(horizontal = GLOBAL_PADDING),
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.75f),
                fontSize = 11.sp
            )
        }
    }
}

@Composable
private fun NewDialog(
    visible: Boolean,
    onDismissRequest: () -> Unit,
) {
}