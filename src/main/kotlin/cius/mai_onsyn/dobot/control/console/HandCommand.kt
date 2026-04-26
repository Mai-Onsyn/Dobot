package cius.mai_onsyn.dobot.control.console

import cius.mai_onsyn.dobot.api.robot.DobotE6V4
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
            api.hand.setPose(handJoint)
        } else {
            val finger = args[0].toInt()
            val joints = api.hand.getPose()?.toIntArray() ?: throw RobotException("获取姿态失败")

            val (targetIdx, rawValue) = if (finger == 1) {
                val typeMap = mapOf("p" to 0, "y" to 1, "r" to 6)
                val idx = typeMap[args[1]] ?: throw IllegalArgumentException("未知类型: ${args[1]}")
                idx to args[2]
            } else {
                finger to args[1]
            }

            val op = rawValue[0]
            val value = if (op in "+-") rawValue.substring(1).toInt() else rawValue.toInt()

            when (op) {
                '+' -> joints[targetIdx] += value
                '-' -> joints[targetIdx] -= value
                else -> joints[targetIdx] = value
            }

            api.hand.setPose(HandJoint.byIntArray(joints))
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
            val joints = api.hand.getPose()?.toIntArray() ?: throw RobotException("获取姿态失败")
            val input = args[0]
            for (finger in 2..5) {
                when (input[0]) {
                    '+' -> joints[finger] += input.substring(1).toInt()
                    '-' -> joints[finger] -= input.substring(1).toInt()
                    else -> joints[finger] = input.toInt()
                }
            }
            api.hand.setPose(HandJoint.byIntArray(joints))
        } else throw IllegalArgumentException("参数错误")
    }
}