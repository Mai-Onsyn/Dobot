package cius.mai_onsyn.dobot.gui.content.trajectory.points

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cius.mai_onsyn.dobot.core.UIInterface.api
import cius.mai_onsyn.dobot.core.trajectory.JointTrajectory
import cius.mai_onsyn.dobot.log
import java.io.File
import java.util.UUID

object TrajectoryPointsManager {

    var file by mutableStateOf<File?>(null)

    val workingTrajectory = mutableStateListOf<PointUiModel>()

    val selectedPointIds = mutableStateListOf<UUID>()

    private val clipBoardPoints = mutableListOf<JointTrajectory.Point>()

    private fun indexOf(id: UUID): Int {
        return workingTrajectory.indexOfFirst { it.id == id }
    }

    private fun selectedIndexes(): List<Int> {
        return selectedPointIds
            .mapNotNull { id ->
                workingTrajectory.indexOfFirst { it.id == id }
                    .takeIf { it >= 0 }
            }
            .sorted()
    }

    fun record() {
        try {
            val trajectory = JointTrajectory().apply {
                record(api.robotApi.calGet, api.robotApi.hand)
            }

            workingTrajectory += trajectory.map { point ->
                PointUiModel(point.copy())
            }
        } catch (e: Exception) {
            log.error("Error recording trajectory", e)
        }
    }

    fun flip() {
        val indexes = selectedIndexes()

        val reversed = indexes
            .map { workingTrajectory[it] }
            .reversed()

        indexes.forEachIndexed { i, index ->
            workingTrajectory[index] = reversed[i]
        }
    }

    fun moveUp() {
        val indexes = selectedIndexes()

        for (index in indexes) {
            if (index <= 0) continue

            val prev = workingTrajectory[index - 1]

            if (prev.id in selectedPointIds) continue

            workingTrajectory.swap(index, index - 1)
        }
    }

    fun moveDown() {
        val indexes = selectedIndexes().reversed()

        for (index in indexes) {
            if (index >= workingTrajectory.lastIndex) continue

            val next = workingTrajectory[index + 1]

            if (next.id in selectedPointIds) continue

            workingTrajectory.swap(index, index + 1)
        }
    }

    fun copy() {
        clipBoardPoints.clear()

        selectedIndexes().forEach { index ->
            clipBoardPoints += workingTrajectory[index]
                .point
                .copy()
        }
    }

    fun cut() {
        copy()

        val ids = selectedPointIds.toSet()

        workingTrajectory.removeAll { it.id in ids }

        selectedPointIds.clear()
    }

    fun pasteUp() {
        if (clipBoardPoints.isEmpty()) return

        val targetIndex = selectedIndexes()
            .firstOrNull()
            ?: workingTrajectory.size

        clipBoardPoints.forEachIndexed { offset, point ->
            workingTrajectory.add(
                targetIndex + offset,
                PointUiModel(point.copy())
            )
        }
    }

    fun pasteDown() {
        if (clipBoardPoints.isEmpty()) return

        val targetIndex = selectedIndexes()
            .lastOrNull()
            ?.plus(1)
            ?: workingTrajectory.size

        clipBoardPoints.forEachIndexed { offset, point ->
            workingTrajectory.add(
                targetIndex + offset,
                PointUiModel(point.copy())
            )
        }
    }

    private fun <T> MutableList<T>.swap(i: Int, j: Int) {
        val temp = this[i]
        this[i] = this[j]
        this[j] = temp
    }
}

data class PointUiModel(
    val point: JointTrajectory.Point,
    val id: UUID = UUID.randomUUID()
)