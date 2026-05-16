package cius.mai_onsyn.dobot.gui.content.trajectory.detail

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cius.mai_onsyn.dobot.gui.GLOBAL_PADDING

// DetailRow.kt
@Composable
fun DetailRow(
    modifier: Modifier = Modifier,
    items: List<Pair<String, Double?>>   // key=标签文字, value=数值(可空)
) {
    Row(modifier = modifier) {
        items.forEachIndexed { index, (text, value) ->
            if (index > 0) {
                Spacer(modifier = Modifier.width(4.dp))
            }
            DetailUnit(
                modifier = Modifier.weight(1f),
                text = text,
                detail = value.toDisplayString()
            )
        }
    }
}

// 扩展函数：将 Double? 转为显示字符串，保留两位小数，空值显示 "——"
private fun Double?.toDisplayString(): String =
    this?.let { "%.0f".format(it) } ?: "——"