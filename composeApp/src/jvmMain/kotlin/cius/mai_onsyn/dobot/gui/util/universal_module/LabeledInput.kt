package cius.mai_onsyn.dobot.gui.util.universal_module

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cius.mai_onsyn.dobot.gui.GLOBAL_PADDING
import cius.mai_onsyn.dobot.gui.ROUND_SMALL_CORNER_SHAPE

@Composable
fun LabeledInput(
    modifier: Modifier = Modifier,
    label: String,
    text: String,
    placeHolderText: String = "",
    onTextChange: (String) -> Unit,
    labelWidth: Dp = 40.dp,
    spacing: Dp = GLOBAL_PADDING
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            textAlign = TextAlign.Right,
            modifier = Modifier.width(labelWidth)
        )
        Spacer(Modifier.width(spacing))
        TextField(
            modifier = Modifier.fillMaxHeight().weight(1f),
            value = text,
            onValueChange = onTextChange,
            backgroundColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            shape = ROUND_SMALL_CORNER_SHAPE,
            shadowElevation = 4.dp
        ) {
            Text(
                text = placeHolderText,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp
            )
        }
    }
}