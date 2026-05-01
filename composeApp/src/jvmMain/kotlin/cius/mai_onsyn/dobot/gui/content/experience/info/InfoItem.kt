package cius.mai_onsyn.dobot.gui.content.experience.info

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InfoItem(
    modifier: Modifier = Modifier,
    title: String,
    content: String,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    extra: @Composable () -> Unit = {}
) {
    Box(
        modifier = modifier.padding(horizontal = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = title,
                modifier = Modifier.weight(1.5f),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 13.sp
            )
            Text(
                text = content,
                modifier = Modifier.weight(2f),
                color = contentColor,
                fontSize = 13.sp
            )
        }
        Box(modifier = Modifier.align(Alignment.CenterEnd)) {
            extra()
        }
    }
}