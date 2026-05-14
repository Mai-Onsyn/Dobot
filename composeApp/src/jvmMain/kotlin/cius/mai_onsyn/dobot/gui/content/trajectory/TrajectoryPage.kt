package cius.mai_onsyn.dobot.gui.content.trajectory

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cius.mai_onsyn.dobot.gui.GLOBAL_PADDING
import cius.mai_onsyn.dobot.gui.content.trajectory.detail.DetailModule
import cius.mai_onsyn.dobot.gui.content.trajectory.file.FileModule
import cius.mai_onsyn.dobot.gui.content.trajectory.operation.OperationModule
import cius.mai_onsyn.dobot.gui.content.trajectory.points.PointsModule
import cius.mai_onsyn.dobot.gui.content.trajectory.replay.ReplayModule

@Composable
fun TrajectoryPage() {
    Row(Modifier.fillMaxSize()) {
        FileModule(Modifier.weight(1f).fillMaxHeight())

        Spacer(Modifier.width(GLOBAL_PADDING))

        PointsModule(Modifier.weight(2f).fillMaxHeight())

        Spacer(Modifier.width(GLOBAL_PADDING))

        Column(Modifier.weight(1f).fillMaxHeight()) {
            DetailModule(Modifier.weight(1f).fillMaxWidth())

            Spacer(Modifier.height(GLOBAL_PADDING))

            OperationModule(Modifier.wrapContentHeight().fillMaxWidth())

            Spacer(Modifier.height(GLOBAL_PADDING))

            ReplayModule(Modifier.weight(1f).fillMaxWidth())
        }
    }
}