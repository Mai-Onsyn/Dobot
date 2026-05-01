package cius.mai_onsyn.dobot.gui.content

import androidx.compose.runtime.Composable
import cius.mai_onsyn.dobot.gui.content.movement.AdjustPage
import cius.mai_onsyn.dobot.gui.content.experience.ExperiencePage
import cius.mai_onsyn.dobot.gui.content.monitor.MonitorPage
import cius.mai_onsyn.dobot.gui.content.settings.SettingsPage
import cius.mai_onsyn.dobot.gui.content.trajectory.TrajectoryPage
import cius.mai_onsyn.dobot.gui.content.welcome.WelcomePage
import dobot.composeapp.generated.resources.Res
import dobot.composeapp.generated.resources.*
import org.jetbrains.compose.resources.DrawableResource

enum class Page(
    val title: String,
    val content: @Composable () -> Unit,
    val icon: DrawableResource
) {
    WELCOME("Welcome", { WelcomePage() }, Res.drawable.icon_welcome),
    MONITOR("监控", { MonitorPage() }, Res.drawable.icon_monitor),
    ADJUST("运动", { AdjustPage() }, Res.drawable.icon_move),
    TRAJECTORY("轨迹", { TrajectoryPage() }, Res.drawable.icon_traj),
    EXPERIENCE("实验", { ExperiencePage() }, Res.drawable.icon_experiment),
    SETTINGS("设置", { SettingsPage() }, Res.drawable.icon_settings)
}