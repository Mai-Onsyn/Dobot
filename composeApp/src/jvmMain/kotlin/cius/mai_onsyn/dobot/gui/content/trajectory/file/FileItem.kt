package cius.mai_onsyn.dobot.gui.content.trajectory.file

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import cius.mai_onsyn.dobot.gui.GLOBAL_PADDING
import cius.mai_onsyn.dobot.gui.ROUND_SMALL_CORNER_SHAPE
import cius.mai_onsyn.dobot.gui.util.buttonEffect
import cius.mai_onsyn.dobot.gui.util.interaction
import cius.mai_onsyn.dobot.gui.util.tweenSpecColor
import cius.mai_onsyn.dobot.gui.util.universal_module.GenericButton
import cius.mai_onsyn.dobot.gui.util.universal_module.PopupContextItem
import cius.mai_onsyn.dobot.gui.util.universal_module.layout.AttachedPopup
import cius.mai_onsyn.dobot.gui.util.universal_module.layout.SingleTextInputDialog
import cius.mai_onsyn.dobot.log
import com.alibaba.fastjson2.JSONArray
import java.awt.Desktop
import java.io.File
import java.io.IOException
import java.nio.file.Files
import kotlin.math.roundToInt

@Composable
fun FileItem(
    modifier: Modifier = Modifier,
    fileName: String,
    selected: Boolean = false,
    onSelect: (String) -> Unit = {}
) {
    val file = File("${TrajectoryFileManager.workingDir}/$fileName")
    if (!file.exists() || file.extension != "json") return
    val json: JSONArray
    try {
         json = JSONArray.parseArray(Files.readString(file.toPath()))
    } catch (e: Exception) {
        log.warn("Unable to parse file $fileName because ${e.message}, skip")
        return
    }
    val background by animateColorAsState(
        if (selected) MaterialTheme.colorScheme.primary.copy(0.7f) else Color.Transparent,
        tweenSpecColor
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(background)
            .buttonEffect(
                hoverAlpha = 0.05f,
                pressedAlpha = 0.1f
            )
            .padding(horizontal = GLOBAL_PADDING)
            .interaction(
                onClick = { onSelect(fileName) },
            )
    ) {
        val contentColor by animateColorAsState(
            if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
            tweenSpecColor
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        ) {
            Text(
                modifier = Modifier.weight(6f),
                text = file.name,
                fontSize = 12.sp,
                color = contentColor,
                textAlign = TextAlign.Left
            )
            Text(
                modifier = Modifier.weight(3f),
                text = json.size.toString(),
                fontSize = 12.sp,
                color = contentColor,
                textAlign = TextAlign.Center
            )
        }

        var buttonHovered by remember { mutableStateOf(false) }
        val density = LocalDensity.current
        var buttonRect by remember { mutableStateOf(IntRect.Zero) }
        var copyProgress by remember { mutableStateOf(0f) }
        val popupShowProgress by animateFloatAsState(
            targetValue = if (buttonHovered) 1f else 0f,
            animationSpec = tween(
                durationMillis = 300,
                delayMillis = if (copyProgress == 0f || copyProgress == 1f) 300 else 0,
                easing = FastOutSlowInEasing
            )
        )
        LaunchedEffect(popupShowProgress) {
            copyProgress = popupShowProgress
        }
        GenericButton(
            modifier = Modifier
                .size(HEIGHT * 0.7f)
                .interaction(
                    onHoveredChange = { buttonHovered = it }
                )
                .onGloballyPositioned {
                    buttonRect = it.boundsInWindow().roundToIntRect()
                },
            shadowElevation = 0.dp,
            shape = ROUND_SMALL_CORNER_SHAPE
        ) {
            Text(
                text = "•••",
                color = contentColor,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        var showRenameDialog by remember { mutableStateOf(false) }
        AttachedPopup(
            triggerLayoutRect = buttonRect,
            showProgress = popupShowProgress,
            shape = ROUND_SMALL_CORNER_SHAPE,
            minWidth = 0.dp,
            minHeight = 0.dp,
            offset = IntOffset(0, (HEIGHT * 0.7f * density.density).value.roundToInt()),
            align = Alignment.TopEnd
        ) {
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .background(MaterialTheme.colorScheme.surface)
                    .interaction(
                        onHoveredChange = { buttonHovered = it }
                    )
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    PopupContextItem(
                        text = "定位到文件",
                        textColor = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(HEIGHT)
                            .interaction(
                                onClick = {
                                    buttonHovered = false
                                    try {
                                        Desktop.getDesktop().browseFileDirectory(file)
                                    } catch (_: Exception) {
                                        val command = when {
                                            System.getProperty("os.name").contains("win", ignoreCase = true) ->
                                                listOf("explorer.exe", "/select,", file.absolutePath)
                                            System.getProperty("os.name").contains("mac", ignoreCase = true) ->
                                                listOf("open", "-R", file.absolutePath)
                                            else ->
                                                listOf("xdg-open", file.parentFile.absolutePath)
                                        }
                                        try {
                                            ProcessBuilder(command).start()
                                        } catch (_: Exception) {
                                            try {
                                                Desktop.getDesktop().open(file.parentFile)
                                            } catch (e: Exception) {
                                                log.error("Failed to open file: $file", e)
                                            }
                                        }
                                    }
                                }
                            )
                    )
                    PopupContextItem(
                        text = "重命名",
                        textColor = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(HEIGHT)
                            .interaction(
                                onClick = {
                                    showRenameDialog = true
                                }
                            )
                    )
                    PopupContextItem(
                        text = "删除",
                        textColor = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(HEIGHT)
                            .interaction(
                                onClick = {
                                    buttonHovered = false
                                    if (file.exists()) {
                                        file.delete()
                                    }
                                    TrajectoryFileManager.update()
                                    if (fileName == TrajectoryFileManager.selectedFile)
                                        TrajectoryFileManager.reselect()
                                }
                            )
                    )
                }
            }
        }

        SingleTextInputDialog(
            visible = showRenameDialog,
            title = "重命名",
            placeholder = "名称",
            initialValue = fileName.removeSuffix(".json"),
            confirmText = "确定",
            suffix = ".json",
            onDismissRequest = { showRenameDialog = false },
            validator = { if (it.isEmpty()) "文件名不能为空" else null }
        ) { name ->
            showRenameDialog = false

            val oldName = File("${TrajectoryFileManager.workingDir}/$fileName")
            val newName = File("${TrajectoryFileManager.workingDir}/$name.json")
            if (newName.exists()) {
                log.error("文件${newName.name}已存在!")
                return@SingleTextInputDialog
            }
            if (oldName.renameTo(newName)) {
                log.info("已将文件${oldName.name}重命名为${newName.name}")
                TrajectoryFileManager.selectedFile = newName.name
                TrajectoryFileManager.update()
            } else {
                log.error("重命名文件${oldName.name} -> ${newName.name}失败")
            }
        }
    }
}