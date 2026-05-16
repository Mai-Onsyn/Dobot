package cius.mai_onsyn.dobot.gui.content.trajectory.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DetailUnit(
    text: String,
    detail: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {         // 宽度由外部 weight 控制
        Text(
            text = text,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.align(Alignment.CenterHorizontally)   // 让标题居中
        )
        Spacer(modifier = Modifier.height(4.dp))
        DetailBox(detail = detail)
    }
}