package cius.mai_onsyn.dobot.gui.content.movement

import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cius.mai_onsyn.dobot.core.UIInterface
import cius.mai_onsyn.dobot.gui.GLOBAL_PADDING
import cius.mai_onsyn.dobot.gui.ROUND_CORNER_SHAPE
import cius.mai_onsyn.dobot.gui.util.background
import cius.mai_onsyn.dobot.gui.util.interaction

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
        Spacer(modifier = Modifier.width(GLOBAL_PADDING))
        VerticalDivider()
        Spacer(modifier = Modifier.width(GLOBAL_PADDING))
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .clip(ROUND_CORNER_SHAPE)
                    .background(MaterialTheme.colorScheme.primary)
                    .interaction(
                        onClick = {
                            UIInterface.app.executeLine("bot connect")
                            UIInterface.app.executeLine("bot setup")
                        }
                    )
            ) {
                Text(
                    text = "Adjust Page",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}