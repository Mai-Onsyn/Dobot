package cius.mai_onsyn.dobot.gui.content.experience.safe_state

import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cius.mai_onsyn.dobot.gui.util.universal_module.CardBase

@Composable
fun SafeStateModule(
    modifier: Modifier = Modifier
) {
    CardBase(
        modifier = modifier
    ) {
        Text(
            text = "Safe State Module",
            modifier = Modifier.align(Alignment.Center),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}