package cius.mai_onsyn.dobot.gui.content.trajectory.points

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun BorderedText(
    modifier: Modifier = Modifier,
    isStart: Boolean = false,
    text: String,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    textStyle: TextStyle = TextStyle()
) {
    BorderedBox(
        modifier = modifier,
        isStart = isStart
    ) {
        Text(
            text = text,
            fontSize = 13.sp,
            color = textColor,
            textAlign = TextAlign.Center,
            style = textStyle,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun BorderedBox(
    modifier: Modifier = Modifier,
    isStart: Boolean = false,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
    ) {
        if (isStart) VerticalDivider(
            Modifier.align(Alignment.CenterStart).fillMaxHeight()
        )
        VerticalDivider(
            Modifier.align(Alignment.CenterEnd).fillMaxHeight()
        )
        HorizontalDivider(
            Modifier.align(Alignment.TopCenter).fillMaxWidth()
        )
        HorizontalDivider(
            Modifier.align(Alignment.BottomCenter).fillMaxWidth()
        )
        content()
    }
}