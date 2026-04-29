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
                CardBase(
                    modifier = Modifier.fillMaxHeight().weight(0.5f)
                ) { ConnectModule(modifier = Modifier.fillMaxSize()) }

                Spacer(modifier = Modifier.width(GLOBAL_PADDING))

                CardBase(
                    modifier = Modifier.fillMaxHeight().weight(0.225f)
                ) { SysStateModule(modifier = Modifier.fillMaxSize()) }

                Spacer(modifier = Modifier.width(GLOBAL_PADDING))

                CardBase(
                    modifier = Modifier.fillMaxHeight().weight(0.3f)
                ) { SafeStateModule(modifier = Modifier.fillMaxSize()) }
            }

            Spacer(modifier = Modifier.width(GLOBAL_PADDING))

            CardBase(
                modifier = Modifier.fillMaxHeight().weight(0.3f)
            ) { InfoModule(modifier = Modifier.fillMaxSize()) }
        }

        Spacer(modifier = Modifier.height(GLOBAL_PADDING))
        Row(
            modifier = Modifier.fillMaxWidth().weight(0.8f),
        ) {
            Column(
                modifier = Modifier.fillMaxHeight().weight(0.7f)
            ) {
                CardBase(
                    modifier = Modifier.fillMaxHeight().weight(0.3f)
                ) { ControlModule(modifier = Modifier.fillMaxSize()) }

                Spacer(modifier = Modifier.height(GLOBAL_PADDING))

                CardBase(
                    modifier = Modifier.fillMaxHeight().weight(1f)
                ) { LogModule(modifier = Modifier.fillMaxSize()) }
            }

            Spacer(modifier = Modifier.width(GLOBAL_PADDING))

            CardBase(
                modifier = Modifier.fillMaxHeight().weight(0.3f)
            ) { FlowModule(modifier = Modifier.fillMaxSize()) }
        }
    }
}