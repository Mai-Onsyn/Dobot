package cius.mai_onsyn.dobot.gui.content.trajectory.operation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cius.mai_onsyn.dobot.gui.content.experience.connect.OperationButton
import cius.mai_onsyn.dobot.gui.util.interaction
import dobot.composeapp.generated.resources.Res
import dobot.composeapp.generated.resources.icon_pump_b
import org.jetbrains.compose.resources.DrawableResource
import javax.swing.Icon

@Composable
fun OperationRow(
    bottonName1: String,
    bottonName2: String,
    icon1: DrawableResource ?= null,
    icon2: DrawableResource ?= null,
    onClick1:()-> Unit={},
    onClick2:()-> Unit={},
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
) {
    Row(
        modifier = modifier
    ){
        OperationButton(
            modifier = Modifier
                .weight(1f)
                .height(40.dp)
                .interaction(onClick = { onClick1()})
            ,
            name = bottonName1,
            icon = icon1
        )
        Spacer(modifier = Modifier.weight(0.1f))
        OperationButton(
            modifier = Modifier
                .weight(1f)
                .height(40.dp)
                .interaction(onClick = { onClick2()}),
            name = bottonName2,
            icon = icon2
        )
    }
}