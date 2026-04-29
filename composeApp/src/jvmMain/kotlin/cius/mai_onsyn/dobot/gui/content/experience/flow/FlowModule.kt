package cius.mai_onsyn.dobot.gui.content.experience.flow

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cius.mai_onsyn.dobot.gui.GLOBAL_PADDING
import cius.mai_onsyn.dobot.gui.util.universal_module.CardBase

@Composable
fun FlowModule(
    modifier: Modifier = Modifier
) {
    CardBase(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(GLOBAL_PADDING)
        ) {
            Text(
                text = "流程步骤",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(GLOBAL_PADDING))
            HorizontalDivider(modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally))
            Spacer(modifier = Modifier.height(12.dp))

            FlowGraph(
                items = listOf("初始化", "姿势准备", "移动烧杯", "蘸取滴定", "记录结果", "结束清理"),
                current = 2,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}