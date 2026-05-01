package cius.mai_onsyn.dobot.gui.content.experience.safe_state

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cius.mai_onsyn.dobot.gui.util.universal_module.CardBase
import dobot.composeapp.generated.resources.Res
import dobot.composeapp.generated.resources.icon_safe_state
import org.jetbrains.compose.resources.painterResource

@Composable
fun SafeStateModule(
    modifier: Modifier = Modifier
) {
    CardBase(
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.dp)
            .fillMaxSize()) {
            Box(modifier = Modifier
                .padding(4.dp)){
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.icon_safe_state),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "安全状态",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 14.sp
                    )
                }
            }
            Spacer(modifier= Modifier.height(12.dp))
            Column (
                modifier = Modifier.fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ){
                SafeLabel(
                    name = "急停状态",
                    statusText = "否",
                    isSafe = true
                )
                Spacer(modifier= Modifier.height(4.dp))
                SafeLabel(
                    name = "掉电状态",
                    statusText = "否",
                    isSafe = true
                )
                Spacer(modifier= Modifier.height(4.dp))
                SafeLabel(
                    name = "碰撞保护",
                    statusText = "正常",
                    isSafe = true
                )
                Spacer(modifier= Modifier.height(4.dp))
                SafeLabel(
                    name = "水位状态",
                    statusText = "充足",
                    isSafe = true
                )
            }
        }
    }
}