package cius.mai_onsyn.dobot.gui.content.experience.connect

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cius.mai_onsyn.dobot.gui.ROUND_CORNER_SHAPE
import cius.mai_onsyn.dobot.gui.util.universal_module.CardBase

@Composable
fun ConnectModule(
    modifier: Modifier = Modifier
) {
    CardBase(
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(start = 12.dp
            ,end = 0.dp, top = 12.dp, bottom = 24.dp
        )
            .fillMaxSize()) {
            Box(modifier = Modifier
                .padding(4.dp)){
                Text(
                    text = "连接状态",
                    modifier = Modifier,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(12.dp)
            )
            Row(modifier= Modifier.fillMaxSize()
            ) {
                Column(modifier = Modifier.weight(1f)
                    .padding(end = 12.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                ) {
                    Spacer(modifier = Modifier.weight(0.4f))
                    DeviceStatusLabel(name = "机械臂",modifier = Modifier.height(40.dp))
                    Spacer(modifier = Modifier.height(15.dp))
                    DeviceStatusLabel(name = "灵巧手",modifier = Modifier.height(40.dp))
                    Spacer(modifier = Modifier.height(15.dp))
                    DeviceStatusLabel(name = "升降台",modifier = Modifier.height(40.dp))
                    Spacer(modifier = Modifier.weight(1f))
                }
                Column(modifier = Modifier.weight(1f)
                    .padding(end = 12.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                ) {
                    Spacer(modifier = Modifier.weight(0.4f))
                    DeviceStatusLabel(name = "蠕动泵",modifier = Modifier.height(40.dp))
                    Spacer(modifier = Modifier.height(15.dp))
                    DeviceStatusLabel(name = "抽水泵",modifier = Modifier.height(40.dp))
                    Spacer(modifier = Modifier.height(15.dp))
                    DeviceStatusLabel(name = "摄像机",modifier = Modifier.height(40.dp))
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}