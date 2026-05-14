package cius.mai_onsyn.dobot.gui.content.trajectory.file

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cius.mai_onsyn.dobot.log
import java.io.File
import java.io.FileWriter

object TrajectoryFileManager {
    val files = mutableStateListOf<String>()
    var selectedFile by mutableStateOf("")
    var workingDir by mutableStateOf("traj2")

    init {
        update()
    }

    fun update() {
        val folder = File(workingDir)
        if (folder.exists() && folder.isDirectory) {
            files.clear()
            folder.listFiles()?.sorted()?.forEach { file ->
                if (file.isFile && file.extension == "json") {
                    files.add(file.name)
                }
            }
        }
        log.debug("Current working directory has ${files.size} trajectory files, current is $selectedFile")
    }

    fun reselect() {
        selectedFile = if (files.isEmpty()) "" else files[0]
    }

    fun create(name: String) {
        val file = File("$workingDir/$name.json")
        if (!file.exists()) {
            file.createNewFile()
            FileWriter(file).use {
                it.write("[]")
            }
            update()
        }
    }
}