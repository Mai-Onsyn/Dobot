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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cius.mai_onsyn.dobot.gui.ROUND_CORNER_SHAPE
import cius.mai_onsyn.dobot.gui.util.universal_module.CardBase
import dobot.composeapp.generated.resources.Res
import dobot.composeapp.generated.resources.icon_arm
import dobot.composeapp.generated.resources.icon_camera
import dobot.composeapp.generated.resources.icon_claw
import dobot.composeapp.generated.resources.icon_connect
import dobot.composeapp.generated.resources.icon_pump_a
import dobot.composeapp.generated.resources.icon_pump_b
import dobot.composeapp.generated.resources.icon_shelf
import org.jetbrains.compose.resources.painterResource

@Composable
fun ConnectModule(
    modifier: Modifier = Modifier
) {
    CardBase(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .padding(
                    start = 12.dp,
                    end = 0.dp,
                    top = 12.dp,
                    bottom = 24.dp
                )
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier.padding(4.dp)
            ){
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.icon_connect),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "连接状态",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 14.sp
                    )
                }
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
                    DeviceStatusLabel(
                        name = "机械臂",
                        icon = Res.drawable.icon_arm,
                        modifier = Modifier.height(40.dp)
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    DeviceStatusLabel(
                        name = "灵巧手",
                        icon = Res.drawable.icon_claw,
                        modifier = Modifier.height(40.dp)
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    DeviceStatusLabel(
                        name = "升降台",
                        icon = Res.drawable.icon_shelf,
                        modifier = Modifier.height(40.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }
                Column(modifier = Modifier.weight(1f)
                    .padding(end = 12.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                ) {
                    Spacer(modifier = Modifier.weight(0.4f))
                    DeviceStatusLabel(
                        name = "蠕动泵",
                        icon = Res.drawable.icon_pump_a,
                        modifier = Modifier.height(40.dp)
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    DeviceStatusLabel(
                        name = "抽水泵",
                        icon = Res.drawable.icon_pump_b,
                        modifier = Modifier.height(40.dp)
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    DeviceStatusLabel(
                        name = "摄像机",
                        icon = Res.drawable.icon_camera,
                        modifier = Modifier.height(40.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}