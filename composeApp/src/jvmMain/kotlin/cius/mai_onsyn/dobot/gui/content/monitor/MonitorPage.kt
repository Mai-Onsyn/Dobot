package cius.mai_onsyn.dobot.gui.content.monitor

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cius.mai_onsyn.dobot.gui.content.monitor.MonitorTab.*
import org.apache.logging.log4j.core.tools.picocli.CommandLine

@Composable
fun MonitorPage() {
    var selectedTab by remember { mutableStateOf(MonitorTab.Robot) }

    Column(modifier = Modifier.fillMaxSize()) {
        PrimaryTabRow(selectedTabIndex = selectedTab.ordinal) {
            MonitorTab.entries.forEach { tab->
                Tab(
                    selected = selectedTab == tab,
                    onClick = { selectedTab = tab },
                    text = { Text(text = tab.title) }
                )
            }
        }
        Box(Modifier.weight(1f)) {
            AnimatedContent(targetState = selectedTab, transitionSpec = {
                fadeIn(tween(200))togetherWith fadeOut(tween(200))
            } ) { tab ->
                when (tab) {
//                    MonitorTab.Robot -> RobotMonitor()
//                    MonitorTab.Pump-> PumpMonitor()
//                    MonitorTab.Hand-> HandMonitor()
                    MonitorTab.Robot-> Text(text = "Robot Monitor")
                    MonitorTab.Pump-> Text(text = "Pump Monitor")
                    MonitorTab.Hand-> Text(text = "Hand Monitor")
                }
        }
    }
    }
}