package cius.mai_onsyn.dobot.gui.content.trajectory.points

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.io.File

object TrajectoryPointsManager {
    var file: File? by mutableStateOf(File(""))
}