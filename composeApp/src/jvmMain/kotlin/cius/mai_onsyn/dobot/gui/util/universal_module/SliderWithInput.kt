package cius.mai_onsyn.dobot.gui.util.universal_module

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cius.mai_onsyn.dobot.gui.ROUND_CORNER_SHAPE
import cius.mai_onsyn.dobot.gui.util.interaction

private val trackHeight = 4.dp
private val thumbSize = 20.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SliderWithInput(
    modifier: Modifier = Modifier,
    value: Float,
    onValueChange: (Float) -> Unit = {},
    range: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
) {
    val colorScheme = MaterialTheme.colorScheme
    var text by remember { mutableStateOf(value.toString()) }
    LaunchedEffect(value) {
        text = "%.2f".format(value)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Slider(
            value = value,
            onValueChange = {
                onValueChange(it)
            },
            valueRange = range,
            steps = steps,
            modifier = Modifier
                .weight(1f),
            track = { sliderPositions ->
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(trackHeight)
                        .clip(ROUND_CORNER_SHAPE)
                        .background(colorScheme.outline.copy(alpha = 0.3f))
                ) {
                    Box(
                        Modifier
                            .fillMaxWidth(sliderPositions.coercedValueAsFraction)
                            .height(trackHeight)
                            .background(colorScheme.primary)
                    )
                }
            },
            thumb = {
                Box(
                    Modifier
                        .size(thumbSize)
                        .background(
                            MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        )
                )
            }
        )
        Spacer(modifier = Modifier.width(8.dp))

        BasicTextField(
            value = text,
            onValueChange = { input ->
                text = input

                val parsed = input.toFloatOrNull()
                if (parsed != null) {
                    val clamped = parsed.coerceIn(range.start, range.endInclusive)
                    onValueChange(clamped)
                }
            },
            cursorBrush = SolidColor(colorScheme.onBackground),
            textStyle = TextStyle(
                color = colorScheme.onBackground,
                fontSize = 13.sp
            ),
            singleLine = true,
            modifier = Modifier
                .width(50.dp)
                .fillMaxHeight()
                .padding(0.dp),
            decorationBox = { innerTextField ->
                Box(
                    contentAlignment = Alignment.CenterStart,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    innerTextField()
                }
            }
        )
    }

}