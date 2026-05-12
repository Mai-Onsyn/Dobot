package cius.mai_onsyn.dobot.gui.content.trajectory.operation

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import cius.mai_onsyn.dobot.gui.util.universal_module.layout.CardBase

@Composable
fun OperationModule(
    modifier: Modifier = Modifier
) {
    CardBase(modifier = modifier) {
        Text(
            text = "Operation Module",
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}