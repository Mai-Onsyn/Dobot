package cius.mai_onsyn.dobot.control.console

import cius.mai_onsyn.dobot.api.DobotE6V4
import cius.mai_onsyn.dobot.robot.arm.Joint

class MovJCommand(private val api: DobotE6V4): Command {
    override val name = "movj"
    override val description = "关节移动"
    override fun execute(args: List<String>) {
        if (args.size == 6 && args.all { it.toDoubleOrNull() != null }) {
            api.move.movJ(Joint(
                args[0].toDouble(), args[1].toDouble(), args[2].toDouble(),
                args[3].toDouble(), args[4].toDouble(), args[5].toDouble()
            ))
            return
        }

        // 情况 B: 局部修改 (例如 j1 5 j3 10)
        val jv = DoubleArray(6)
        // 获取当前角度作为基准
        api.calGet.getAngle().refValues.forEachIndexed { index, param ->
            jv[index] = (param.value as Number).toDouble()
        }

        var i = 0
        while (i < args.size) {
            val key = args[i].lowercase()
            val valueStr = args.getOrNull(i + 1)

            val jointIndex = Joint.getJointIndex(key)
            val value = valueStr?.toDoubleOrNull()

            if (jointIndex != -1 && value != null) {
                jv[jointIndex] = value
                i += 2
            } else {
                i++
            }
        }

        api.move.movJ(Joint(jv[0], jv[1], jv[2], jv[3], jv[4], jv[5]))
    }
}

class MovJogCommand(private val api: DobotE6V4): Command {
    override val name = "movjog"
    override val description = "关节增量移动"
    override fun execute(args: List<String>) {
        val jv = DoubleArray(6)
        api.calGet.getAngle().refValues.forEachIndexed { index, param ->
            jv[index] = (param.value as Number).toDouble()
        }

        var i = 0
        while (i < args.size) {
            val key = args[i].lowercase()
            val valueStr = args.getOrNull(i + 1)

            val jointIndex = Joint.getJointIndex(key)
            val value = valueStr?.toDoubleOrNull()

            if (jointIndex != -1 && value != null) {
                jv[jointIndex] += value // 增量累加
                i += 2
            } else {
                i++
            }
        }
        api.move.movJ(Joint(jv[0], jv[1], jv[2], jv[3], jv[4], jv[5]))
    }
}