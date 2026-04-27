package cius.mai_onsyn.dobot.trajectory

import cius.mai_onsyn.dobot.core.api.robot.HandApi
import cius.mai_onsyn.dobot.core.api.robot.RobotCalGetApi
import cius.mai_onsyn.dobot.core.api.robot.RobotMoveApi
import cius.mai_onsyn.dobot.robot.arm.Joint
import cius.mai_onsyn.dobot.robot.hand.HandJoint
import cius.mai_onsyn.dobot.log
import com.alibaba.fastjson2.JSONObject
import com.alibaba.fastjson2.JSONArray
import com.alibaba.fastjson2.JSONWriter
import java.io.File
import java.nio.file.Files


class JointTrajectory() : Trajectory, ArrayList<Pair<Joint, HandJoint>>() {

    constructor(list: List<Pair<Joint, HandJoint>>) : this() {
        addAll(list)
    }

    companion object {
        fun fromJSON(json: JSONArray): JointTrajectory {
            val list = json.mapNotNull { item ->
                val obj = item as? JSONObject ?: return@mapNotNull null

                val jointStr = obj.getString("joint")
                val handStr = obj.getString("hand")

                val joint = jointStr?.let { Joint.of(it) }
                val hand = handStr?.let { HandJoint.of(it) } ?: HandJoint.OPEN

                if (joint != null) Pair(joint, hand) else null
            }
            return JointTrajectory(list)
        }

        fun load(file: String): JointTrajectory {
            val content = Files.readString(File(file).toPath())
            return fromJSON(JSONArray.parseArray(content))
        }
    }

    override fun record(api: RobotCalGetApi, hand: HandApi): Boolean {
        val jointData = api.getAngle().refValues.joinToString(",")
        val jointRecord = Joint.of(jointData)
        val handRecord = hand.getPose()

        if (jointRecord != null &&
            handRecord != null &&
            (jointRecord != lastOrNull()?.first ||
            handRecord != lastOrNull()?.second)
            ) {
            add(Pair(jointRecord, handRecord))
            return true
        } else {
            log.warn("无法获取当前关节点，或当前点与上一个相同，不记录")
            return false
        }
    }

    override fun replay(api: RobotMoveApi, hand: HandApi) {
        var lastHand: HandJoint? = null
        forEach { (joint, pose) ->
            api.movJ(joint, block = true)
            hand.setPose(pose)
            if (lastHand != null && lastHand != pose) {
                Thread.sleep(2000)
            }
            lastHand = pose
        }
    }

    override fun toJSON(): JSONArray {
        val array = JSONArray()
        forEach { (joint, hand) ->
            val obj = JSONObject()
            obj["joint"] = joint.toString()
            obj["hand"] = hand.toString()
            array.add(obj)
        }
        return array
    }

    fun write(file: String) {
        val f = File(file)
        if (!f.exists()) f.createNewFile()

        Files.writeString(
            f.toPath(),
            toJSON().toJSONString(JSONWriter.Feature.PrettyFormat)
        )
    }
}