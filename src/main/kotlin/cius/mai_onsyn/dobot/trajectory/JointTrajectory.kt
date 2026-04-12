package cius.mai_onsyn.dobot.trajectory

import cius.mai_onsyn.dobot.api.RobotCalGetApi
import cius.mai_onsyn.dobot.api.RobotMoveApi
import cius.mai_onsyn.dobot.robot.arm.Joint
import com.alibaba.fastjson2.JSONArray
import com.alibaba.fastjson2.JSONWriter
import java.io.File
import java.nio.file.Files

class JointTrajectory(): Trajectory, ArrayList<Joint>() {

    constructor(list: List<Joint>) : this() {
        addAll(list)
    }

    companion object {
        fun fromJSON(json: JSONArray): JointTrajectory {
            return JointTrajectory(json.mapNotNull { Joint.of(it.toString()) })
        }

        fun load(file: String): JointTrajectory {
            return fromJSON(JSONArray.parseArray(
                Files.readString(File(file).toPath())
            ))
        }
    }

    override fun record(api: RobotCalGetApi) {
        val record = Joint.of(api.getAngle().refValues.joinToString(","))
        if (record != null && record != lastOrNull()) add(record)
    }

    override fun replay(api: RobotMoveApi) {
        forEach {
            api.movJ(it)
//            Thread.sleep(1000)
        }
    }

    override fun toJSON(): JSONArray {
        return JSONArray.of(*map { it.toString() }.toTypedArray())
    }

    fun write(file: String) {
        val f = File(file)
        if (!f.exists()) f.createNewFile()
        Files.writeString(
            f.toPath(),
            toJSON().toString(JSONWriter.Feature.PrettyFormat)
        )
    }
}