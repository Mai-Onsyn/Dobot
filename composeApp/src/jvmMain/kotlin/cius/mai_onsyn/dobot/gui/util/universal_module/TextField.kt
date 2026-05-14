package cius.mai_onsyn.dobot.gui.util.universal_module

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cius.mai_onsyn.dobot.gui.GLOBAL_PADDING_HALF

@Composable
fun TextField(
    modifier: Modifier = Modifier,
    value: String = "",
    onValueChange: (String) -> Unit = {},
    backgroundColor: Color = Color.Transparent,
    shape: Shape = RectangleShape,
    shadowElevation: Dp = 0.dp,
    borderColor: Color = Color.Transparent,
    borderWidth: Dp = 0.dp,
    maxLines: Int = 1,
    paddingValues: PaddingValues = PaddingValues(horizontal = GLOBAL_PADDING_HALF),
    textStyle: TextStyle = TextStyle(fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface),
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    placeholder: @Composable (() -> Unit)? = null,
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = textStyle.copy(color = contentColor),
        singleLine = maxLines == 1,
        maxLines = maxLines,
        cursorBrush = SolidColor(contentColor),
        modifier = modifier
            .shadow(elevation = shadowElevation, ambientColor = Color.Black)
            .background(backgroundColor, shape)
            .border(borderWidth, borderColor, shape),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .padding(paddingValues),
                contentAlignment = Alignment.CenterStart
            ) {
                if (value.isEmpty() && placeholder != null) {
                    placeholder()
                }
                innerTextField()
            }
        }
    )
}