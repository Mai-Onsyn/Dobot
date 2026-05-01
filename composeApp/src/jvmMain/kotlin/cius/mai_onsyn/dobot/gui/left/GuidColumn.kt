package cius.mai_onsyn.dobot.gui.left

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.Icon
import androidx.compose.material.NavigationRail
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cius.mai_onsyn.dobot.gui.ROUND_CORNER_SHAPE
import cius.mai_onsyn.dobot.gui.content.Page
import cius.mai_onsyn.dobot.gui.currentPage
import cius.mai_onsyn.dobot.gui.util.appShadow
import org.jetbrains.compose.resources.painterResource

@Composable
fun GuidColumn(
    modifier: Modifier = Modifier
) {
    val theme = MaterialTheme.colorScheme
    Column(
        modifier = modifier
            .appShadow()
            .fillMaxHeight()
            .background(theme.surfaceContainer)
            .padding(top = 16.dp)
    ) {
        Spacer(Modifier.weight(0.5f))
        Text(
            text = "控制台",
            fontSize = 20.sp,
            color = theme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Page.entries.forEach {
            Spacer(modifier = Modifier.height(2.dp))
            Button(
                onClick = { currentPage = it },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (currentPage != it) theme.surfaceContainer else theme.secondaryContainer,
                    contentColor = theme.surfaceVariant
                ),
                shape = ROUND_CORNER_SHAPE,
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Icon(
                    painter = painterResource(it.icon),
                    contentDescription = null,
                    tint = theme.onSurface,
                    modifier = Modifier.size(20.dp)
                )
//                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = it.title,
                    fontSize = 14.sp,
                    color = theme.onSurface,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterVertically)
                )
            }
        }
        Spacer(Modifier.weight(1f))
    }
}