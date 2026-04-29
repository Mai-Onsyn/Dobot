package cius.mai_onsyn.dobot.gui.content.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cius.mai_onsyn.dobot.gui.GLOBAL_PADDING
import cius.mai_onsyn.dobot.gui.top.ThemeSwitch
import cius.mai_onsyn.dobot.gui.util.Config.isDarkMode

@Composable
fun SettingsPage() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .align(Alignment.Center)
        ) {
            Text(
                text = "Settings Page",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(GLOBAL_PADDING))

            ThemeSwitch(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                isDark = isDarkMode,
                onToggle = { isDarkMode = it }
            )
        }
    }
}