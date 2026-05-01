package cius.mai_onsyn.dobot.gui.content.experience.log

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dobot.composeapp.generated.resources.Res
import dobot.composeapp.generated.resources.icon_search
import org.jetbrains.compose.resources.painterResource

@Composable
fun SearchInput(
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme

    Box(modifier = modifier
        .border(
            width = 1.dp,
            color = colorScheme.outline.copy(0.2f),
            shape = RoundedCornerShape(8.dp)
        )
        .clip(RoundedCornerShape(8.dp))
        .background(colorScheme.surfaceContainerHigh)
    ) {

        var text by remember { mutableStateOf("") }

        BasicTextField(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 8.dp),
            value = text,
            onValueChange = { text = it },
            singleLine = true,
            cursorBrush = SolidColor(colorScheme.onSurface),
            textStyle = TextStyle(
                color = colorScheme.onSurface,
                fontSize = 13.sp
            ),
            decorationBox = { innerTextField ->

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(Res.drawable.icon_search),
                        contentDescription = null,
                        tint = colorScheme.onSurface,
                        modifier = Modifier
                            .size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (text.isEmpty()) {
                            Text(
                                text = "搜索日志...",
                                color = colorScheme.onSurfaceVariant,
                                fontSize = 13.sp
                            )
                        }
                        innerTextField()
                    }
                }
            }
        )
    }
}