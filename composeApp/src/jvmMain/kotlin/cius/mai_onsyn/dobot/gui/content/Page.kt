package cius.mai_onsyn.dobot.gui.content

import androidx.compose.runtime.Composable
import cius.mai_onsyn.dobot.gui.content.movement.AdjustPage
import cius.mai_onsyn.dobot.gui.content.experience.ExperiencePage
import cius.mai_onsyn.dobot.gui.content.monitor.MonitorPage
import cius.mai_onsyn.dobot.gui.content.settings.SettingsPage
import cius.mai_onsyn.dobot.gui.content.trajectory.TrajectoryPage
import cius.mai_onsyn.dobot.gui.content.welcome.WelcomePage

enum class Page(
    val title: String,
    val content: @Composable () -> Unit
) {
    WELCOME("Welcome", { WelcomePage() }),
    MONITOR("监控", { MonitorPage() }),
    ADJUST("运动", { AdjustPage() }),
    TRAJECTORY("轨迹", { TrajectoryPage() }),
    EXPERIENCE("实验", { ExperiencePage() }),
    SETTINGS("设置", { SettingsPage() })
}