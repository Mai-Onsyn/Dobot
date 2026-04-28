package cius.mai_onsyn.dobot.gui.util.universal_module

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import cius.mai_onsyn.dobot.gui.ROUND_CORNER_SHAPE

private val trackHeight = 4.dp
private val thumbSize = 20.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThickSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    range: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    enabled: Boolean = true
) {
    val colorScheme = MaterialTheme.colorScheme

    Slider(
        value = value,
        onValueChange = onValueChange,
        valueRange = range,
        steps = steps,
        enabled = enabled,
        modifier = modifier,
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
                        colorScheme.primary,
                        shape = CircleShape
                    )
            )
        }
    )
}