package cius.mai_onsyn.dobot.gui.content.trajectory.file

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cius.mai_onsyn.dobot.gui.GLOBAL_PADDING
import cius.mai_onsyn.dobot.gui.GLOBAL_PADDING_HALF
import cius.mai_onsyn.dobot.gui.ROUND_CORNER_SHAPE
import cius.mai_onsyn.dobot.gui.ROUND_SMALL_CORNER_SHAPE
import cius.mai_onsyn.dobot.gui.util.interaction
import cius.mai_onsyn.dobot.gui.util.universal_module.ButtonWithIcon
import cius.mai_onsyn.dobot.gui.util.universal_module.GenericButton
import cius.mai_onsyn.dobot.gui.util.universal_module.layout.CardBase
import cius.mai_onsyn.dobot.gui.util.universal_module.SearchInput
import cius.mai_onsyn.dobot.gui.util.universal_module.TextField
import cius.mai_onsyn.dobot.gui.util.universal_module.layout.DialogPopup
import dobot.composeapp.generated.resources.Res
import dobot.composeapp.generated.resources.icon_reset
import java.awt.Toolkit
import java.io.File
import java.io.FileWriter

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
                    NewDialog(
                        visible = showNewDialog,
                        onDismissRequest = { showNewDialog = false }
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

@Composable
private fun NewDialog(
    visible: Boolean,
    onDismissRequest: () -> Unit,
) {
    DialogPopup(
        visible = visible,
        onDismissRequest = onDismissRequest,
    ) {
        var showError by remember { mutableStateOf(false) }
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Column(
                Modifier.align(Alignment.Center)
                    .padding(horizontal = 48.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                var filename by remember { mutableStateOf("") }
                Text(
                    text = "新建轨迹文件",
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(Modifier.height(GLOBAL_PADDING * 2))
                Row(
                    modifier = Modifier.size(240.dp, 32.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TextField(
                        modifier = Modifier.fillMaxHeight().weight(1f),
                        value = filename,
                        onValueChange = { filename = it },
                        textStyle = MaterialTheme.typography.bodyMedium,
                        backgroundColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                        shape = ROUND_SMALL_CORNER_SHAPE,
                        borderColor = if (showError) MaterialTheme.colorScheme.error else Color.Transparent,
                        shadowElevation = 8.dp
                    ) {
                        Text(
                            text = if (showError) "文件名不能为空" else "轨迹名称",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                    }
                    Spacer(Modifier.width(GLOBAL_PADDING_HALF))
                    Text(
                        text = ".json",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(Modifier.height(GLOBAL_PADDING * 2))
                GenericButton(
                    modifier = Modifier
                        .size(60.dp, 32.dp)
                        .interaction(
                            onClick = {
                                if (!filename.isEmpty()) {
                                    TrajectoryFileManager.create(filename)
                                    TrajectoryFileManager.selectedFile = "$filename.json"
                                    onDismissRequest()
                                } else {
                                    showError = true
                                    Toolkit.getDefaultToolkit().beep()
                                }
                            }
                        ),
                    backgroundColor = MaterialTheme.colorScheme.secondary,
                    shape = ROUND_SMALL_CORNER_SHAPE,
                ) {
                    Text(
                        text = "创建",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }
        }
    }
}