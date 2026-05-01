package cius.mai_onsyn.dobot.gui.content.experience.flow

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
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
import dobot.composeapp.generated.resources.Res
import dobot.composeapp.generated.resources.icon_list
import org.jetbrains.compose.resources.painterResource

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
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(Res.drawable.icon_list),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "流程步骤",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(GLOBAL_PADDING))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(12.dp))

            FlowGraph(
                items = listOf("初始化", "姿势准备", "移动烧杯", "蘸取滴定", "记录结果", "结束清理"),
                current = 2,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}