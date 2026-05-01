package cius.mai_onsyn.dobot.gui.content.experience.sys_state

import StatusCheckCircle
import androidx.compose.foundation.layout.*
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
import dobot.composeapp.generated.resources.icon_sys_state
import org.jetbrains.compose.resources.painterResource

@Composable
fun SysStateModule(
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
                        painter = painterResource(Res.drawable.icon_sys_state),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "系统状态",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 14.sp
                    )
                }
            }
            Spacer(modifier= Modifier.height(12.dp))
            StatusCheckCircle()
        }
    }
}