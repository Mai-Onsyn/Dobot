package cius.mai_onsyn.dobot.gui.content.experience.sys_state

import StatusCheckCircle
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.unit.sp
import cius.mai_onsyn.dobot.gui.content.experience.safe_state.SafeLable
import cius.mai_onsyn.dobot.gui.util.universal_module.CardBase

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
                Text(text = "系统状态",
                    modifier= Modifier,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp
                )
            }
            Spacer(modifier= Modifier.height(12.dp))
            StatusCheckCircle()
        }
    }
}