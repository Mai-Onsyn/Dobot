package cius.mai_onsyn.dobot.gui.content.trajectory.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DetailBox(
    detail: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()                                    // ★ 填满父容器宽度
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                shape = RoundedCornerShape(4.dp)
            )
            .border(
                1.dp,
                MaterialTheme.colorScheme.outline.copy(0.2f),
                RoundedCornerShape(4.dp)
            )
            .padding(horizontal = 4.dp, vertical = 4.dp),
        contentAlignment = Alignment.CenterStart               // 内容左对齐
    ) {
        Text(text = detail, color = MaterialTheme.colorScheme.onSurface)
    }
}