package cius.mai_onsyn.dobot.gui.content.trajectory.operation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cius.mai_onsyn.dobot.gui.GLOBAL_PADDING
import cius.mai_onsyn.dobot.gui.WHITE_COLOR
import cius.mai_onsyn.dobot.gui.content.experience.connect.OperationButton
import cius.mai_onsyn.dobot.gui.content.experience.control.MorphingPlayPauseButton
import cius.mai_onsyn.dobot.gui.experimenting
import cius.mai_onsyn.dobot.gui.util.universal_module.layout.CardBase
import dobot.composeapp.generated.resources.Res
import dobot.composeapp.generated.resources.icon_pump_b

@Composable
fun OperationModule(
    modifier: Modifier = Modifier
) {
    CardBase(modifier = modifier, fillMaxSize = false) {
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(vertical = GLOBAL_PADDING),
        ){
            Text(
                text = "点位操作",
                style = TextStyle(fontWeight = FontWeight.Bold),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = GLOBAL_PADDING).height(24.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = GLOBAL_PADDING)
            ){

                OperationRow("编辑","重置")
                Spacer(modifier = Modifier.height(12.dp))
                OperationRow("上移","下移")
                Spacer(modifier = Modifier.height(12.dp))
                OperationRow("翻转","记录当前点")
                Spacer(modifier = Modifier.height(12.dp))
                OperationRow("向上粘贴","向下粘贴")
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

    }
}