package cius.mai_onsyn.dobot.gui.util.universal_module

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cius.mai_onsyn.dobot.gui.GLOBAL_PADDING
import cius.mai_onsyn.dobot.gui.ROUND_SMALL_CORNER_SHAPE
import cius.mai_onsyn.dobot.gui.util.buttonEffect
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun PopupContextItem(
    modifier: Modifier = Modifier,
    icon: DrawableResource? = null,
    text: String,
    textColor: Color
) {
    Box(
        modifier = modifier
            .clip(ROUND_SMALL_CORNER_SHAPE)
            .buttonEffect()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.Center)
        ) {
            if (icon != null) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = textColor,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(GLOBAL_PADDING))
            }
            Text(
                text = text,
                fontSize = 13.sp,
                color = textColor
            )
        }
    }
}