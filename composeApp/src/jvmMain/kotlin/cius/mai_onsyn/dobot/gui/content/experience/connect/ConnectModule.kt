package cius.mai_onsyn.dobot.gui.content.experience.connect

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cius.mai_onsyn.dobot.gui.util.universal_module.CardBase

@Composable
fun ConnectModule(
    modifier: Modifier = Modifier
) {
    CardBase(
        modifier = modifier
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "连接状态",
                modifier = Modifier.align(Alignment.TopStart),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}