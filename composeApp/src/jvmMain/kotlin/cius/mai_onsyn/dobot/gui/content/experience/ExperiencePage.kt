package cius.mai_onsyn.dobot.gui.content.experience

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cius.mai_onsyn.dobot.gui.GLOBAL_PADDING
import cius.mai_onsyn.dobot.gui.content.experience.connect.ConnectModule
import cius.mai_onsyn.dobot.gui.content.experience.control.ControlModule
import cius.mai_onsyn.dobot.gui.content.experience.flow.FlowModule
import cius.mai_onsyn.dobot.gui.content.experience.info.InfoModule
import cius.mai_onsyn.dobot.gui.content.experience.log.LogModule
import cius.mai_onsyn.dobot.gui.content.experience.safe_state.SafeStateModule
import cius.mai_onsyn.dobot.gui.content.experience.sys_state.SysStateModule
import cius.mai_onsyn.dobot.gui.util.universal_module.CardBase

@Composable
fun ExperiencePage() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().weight(0.4f),
        ) {
            Row(
                modifier = Modifier.fillMaxHeight().weight(0.7f)
            ) {
                ConnectModule(modifier = Modifier.fillMaxHeight().weight(0.5f))

                Spacer(modifier = Modifier.width(GLOBAL_PADDING))

                SysStateModule(modifier = Modifier.fillMaxHeight().weight(0.225f))

                Spacer(modifier = Modifier.width(GLOBAL_PADDING))

                SafeStateModule(modifier = Modifier.fillMaxHeight().weight(0.3f))
            }

            Spacer(modifier = Modifier.width(GLOBAL_PADDING))

            InfoModule(modifier = Modifier.fillMaxHeight().weight(0.3f))
        }

        Spacer(modifier = Modifier.height(GLOBAL_PADDING))
        Row(
            modifier = Modifier.fillMaxWidth().weight(0.8f),
        ) {
            Column(
                modifier = Modifier.fillMaxHeight().weight(0.7f)
            ) {
                ControlModule(modifier = Modifier.fillMaxHeight().weight(0.3f))

                Spacer(modifier = Modifier.height(GLOBAL_PADDING))

                LogModule(modifier = Modifier.fillMaxHeight().weight(1f))
            }

            Spacer(modifier = Modifier.width(GLOBAL_PADDING))

            FlowModule(modifier = Modifier.fillMaxHeight().weight(0.3f))
        }
    }
}