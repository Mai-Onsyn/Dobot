package cius.mai_onsyn.dobot.gui.util.universal_module.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cius.mai_onsyn.dobot.gui.GLOBAL_PADDING
import cius.mai_onsyn.dobot.gui.GLOBAL_PADDING_HALF
import cius.mai_onsyn.dobot.gui.ROUND_SMALL_CORNER_SHAPE
import cius.mai_onsyn.dobot.gui.util.interaction
import cius.mai_onsyn.dobot.gui.util.universal_module.GenericButton
import cius.mai_onsyn.dobot.gui.util.universal_module.TextField
import java.awt.Toolkit

@Composable
fun SingleTextInputDialog(
    visible: Boolean,
    title: String,
    initialValue: String = "",
    placeholder: String = "请输入",
    confirmText: String = "确定",
    suffix: String? = null,
    onDismissRequest: () -> Unit,
    validator: (String) -> String? = { if (it.isBlank()) "内容不能为空" else null },
    onConfirm: (String) -> Unit
) {
    DialogPopup(
        visible = visible,
        onDismissRequest = onDismissRequest,
    ) {
        var inputText by remember(visible) { mutableStateOf(initialValue) }
        var errorHint by remember(visible) { mutableStateOf<String?>(null) }

        Box(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface)
        ) {
            Column(
                Modifier.align(Alignment.Center)
                    .padding(horizontal = 48.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(Modifier.height(GLOBAL_PADDING * 2))

                Row(
                    modifier = Modifier.size(240.dp, 32.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TextField(
                        modifier = Modifier.fillMaxHeight().weight(1f),
                        value = inputText,
                        onValueChange = {
                            inputText = it
                            errorHint = null
                        },
                        textStyle = MaterialTheme.typography.bodyMedium,
                        backgroundColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                        shape = ROUND_SMALL_CORNER_SHAPE,
                        borderColor = if (errorHint != null) MaterialTheme.colorScheme.error else Color.Transparent,
                        shadowElevation = 8.dp
                    ) {
                        Text(
                            text = errorHint ?: placeholder,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (errorHint != null)
                                MaterialTheme.colorScheme.error
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                    }

                    suffix?.let {
                        Spacer(Modifier.width(GLOBAL_PADDING_HALF))
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(Modifier.height(GLOBAL_PADDING * 2))

                GenericButton(
                    modifier = Modifier
                        .size(80.dp, 32.dp)
                        .interaction(
                            onClick = {
                                val validationResult = validator(inputText)
                                if (validationResult == null) {
                                    onConfirm(inputText)
                                } else {
                                    errorHint = validationResult
                                    Toolkit.getDefaultToolkit().beep()
                                }
                            }
                        ),
                    backgroundColor = MaterialTheme.colorScheme.secondary,
                    shape = ROUND_SMALL_CORNER_SHAPE,
                ) {
                    Text(
                        text = confirmText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }
        }
    }
}