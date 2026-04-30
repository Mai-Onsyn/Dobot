package cius.mai_onsyn.dobot.gui.content.experience.log

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cius.mai_onsyn.dobot.gui.GLOBAL_PADDING
import cius.mai_onsyn.dobot.gui.ROUND_CORNER_SHAPE
import cius.mai_onsyn.dobot.gui.util.background
import cius.mai_onsyn.dobot.gui.util.universal_module.CardBase

@Composable
fun LogModule(
    modifier: Modifier = Modifier
) {
    CardBase(
        modifier = modifier
    ) {
        Column {
            Box(
                modifier = Modifier
                    .height(42.dp)
                    .padding(vertical = 8.dp, horizontal = 16.dp)
            ) {
                Text(
                    text = "日志输出",
                    modifier = Modifier
                        .align(Alignment.CenterStart),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    modifier = Modifier
//                        .align(Alignment.CenterEnd)
//                        .fillMaxHeight()
////                        .fillMaxSize(),
//                ) {
//                    SearchInput(
//                        modifier = Modifier.size(width = 192.dp, height = 36.dp)
//                    )
//                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            HorizontalDivider(modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(4.dp))

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp)
                    .clip(ROUND_CORNER_SHAPE)
                    .background(MaterialTheme.colorScheme.surfaceContainerLow)
            )
        }
    }
}