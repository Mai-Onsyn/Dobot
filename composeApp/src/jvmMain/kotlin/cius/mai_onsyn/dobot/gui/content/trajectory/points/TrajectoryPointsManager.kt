package cius.mai_onsyn.dobot.gui.content.trajectory.points

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cius.mai_onsyn.dobot.core.trajectory.JointTrajectory
import java.io.File

object TrajectoryPointsManager {
    var file: File? by mutableStateOf(null)

    var selectedPoint: JointTrajectory.Point? by mutableStateOf(null)
    val selectedPoints = mutableStateListOf<JointTrajectory.Point>()
}