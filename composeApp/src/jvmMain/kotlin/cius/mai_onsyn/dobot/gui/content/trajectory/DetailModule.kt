package cius.mai_onsyn.dobot.gui.content.trajectory

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import cius.mai_onsyn.dobot.gui.util.universal_module.CardBase

@Composable
fun DetailModule(
    modifier: Modifier = Modifier
) {
    CardBase(modifier = modifier) {
        Text(
            text = "Detail Module",
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}