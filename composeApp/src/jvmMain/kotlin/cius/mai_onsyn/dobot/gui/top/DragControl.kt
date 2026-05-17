package cius.mai_onsyn.dobot.gui.top

import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cius.mai_onsyn.dobot.core.UIInterface.app
import cius.mai_onsyn.dobot.gui.BLUE_COLOR
import cius.mai_onsyn.dobot.gui.ROUND_SMALL_CORNER_SHAPE
import cius.mai_onsyn.dobot.gui.util.interaction
import cius.mai_onsyn.dobot.gui.util.universal_module.GenericButton

@Composable
fun DragControl(
    isEnable: Boolean,
    bg: Color
) {
    GenericButton(
        shape = ROUND_SMALL_CORNER_SHAPE,
        backgroundColor = bg,
        modifier = Modifier
            .size(88.dp, 36.dp)
            .interaction(
                onClick = {
                    app.executeLine("bot ${if (isEnable) "start" else "stop"}drag")
                }
            ),
    ) {
        Text(
            text = "${if (isEnable) "Start" else "Stop"} Drag",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}