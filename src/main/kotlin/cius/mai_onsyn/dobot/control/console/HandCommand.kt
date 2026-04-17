package cius.mai_onsyn.dobot.control.console

import cius.mai_onsyn.dobot.api.DobotE6V4
import cius.mai_onsyn.dobot.api.socket.RobotException
import cius.mai_onsyn.dobot.robot.hand.HandJoint

class FingerCommand(
    private val api: DobotE6V4
): Command {
    override val name = "finger"
    override val description = "设置单根手指关节"

    override fun execute(args: List<String>) {
        if (args.size == 7) {
            val handJoint = HandJoint(
                args[0].toInt(),
                args[1].toInt(),
                args[2].toInt(),
                args[3].toInt(),
                args[4].toInt(),
                args[5].toInt(),
                args[6].toInt()
            )
            api.handApi.setPose(handJoint)
        } else {
            val finger = args[0].toInt()

            val joints = api.handApi.getPose()?.toIntArray() ?: throw RobotException("获取姿态失败")
            if (finger == 1) {
                val type = args[1]
                val value = args[2].toInt()
                when (type) {
                    "pitch" -> joints[0] = value
                    "yaw" -> joints[1] = value
                    "roll" -> joints[6] = value
                }
            } else {
                joints[finger] = args[1].toInt()
            }

            api.handApi.setPose(HandJoint.byIntArray(joints))
        }
    }
}

class HandCommand(
    private val api: DobotE6V4
): Command {
    override val name = "hand"
    override val description = "一起设置除了大拇指以外的4根手指"

    override fun execute(args: List<String>) {
        if (args.size == 1) {
            val joints = api.handApi.getPose()?.toIntArray() ?: throw RobotException("获取姿态失败")
            joints[2] = args[0].toInt()
            joints[3] = args[0].toInt()
            joints[4] = args[0].toInt()
            joints[5] = args[0].toInt()
            api.handApi.setPose(HandJoint.byIntArray(joints))
        } else throw IllegalArgumentException("参数错误")
    }
}