package cius.mai_onsyn.dobot.gui.content.movement

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun AdjustPage() {
    Row(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .weight(0.7f)
                .fillMaxSize()
        ) {
//            Text(
//                text = "Adjust Page",
//                textAlign = TextAlign.Center,
//                color = MaterialTheme.colorScheme.onBackground,
//                modifier = Modifier.align(Alignment.Center)
//            )
            ArmControlPanel()
        }
        Spacer(modifier = Modifier.width(16.dp))
        VerticalDivider()
        Spacer(modifier = Modifier.width(16.dp))
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        ) {
            Text(
                text = "Adjust Page",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}