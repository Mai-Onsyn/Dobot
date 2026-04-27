package cius.mai_onsyn.dobot.gui.content

import androidx.compose.runtime.Composable
import cius.mai_onsyn.dobot.gui.content.debug.AdjustPage
import cius.mai_onsyn.dobot.gui.content.experience.ExperiencePage
import cius.mai_onsyn.dobot.gui.content.monitor.MonitorPage
import cius.mai_onsyn.dobot.gui.content.welcome.WelcomePage

enum class Page(
    val title: String,
    val content: @Composable () -> Unit
) {
    WELCOME("Welcome", { WelcomePage() }),
    MONITOR("Monitor", { MonitorPage() }),
    ADJUST("Adjust", { AdjustPage() }),
    EXPERIENCE("Experience", { ExperiencePage() }),
}