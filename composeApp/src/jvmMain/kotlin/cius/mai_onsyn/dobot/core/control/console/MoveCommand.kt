package cius.mai_onsyn.dobot.core.control.console

import cius.mai_onsyn.dobot.core.api.robot.DobotE6V4
import cius.mai_onsyn.dobot.core.robot.arm.Coordinate
import cius.mai_onsyn.dobot.core.robot.arm.Joint

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

class MovLCommand(private val api: DobotE6V4) : Command {
    override val name = "movl"
    override val description = "直线移动"

    override fun execute(args: List<String>) {
        // 情况 A: 全参数输入 (x y z rx ry rz)
        if (args.size == 6 && args.all { it.toDoubleOrNull() != null }) {
            api.move.movL(Coordinate(
                args[0].toDouble(), args[1].toDouble(), args[2].toDouble(),
                args[3].toDouble(), args[4].toDouble(), args[5].toDouble()
            ))
            return
        }

        // 情况 B: 局部修改 (例如 x 100 z 50)
        val cv = DoubleArray(6)
        // 获取当前笛卡尔位姿作为基准
        val currentPose = api.calGet.getPose()
        for (i in 0..5)
            cv[i] = currentPose.refValues[i].value as Double

        var i = 0
        while (i < args.size) {
            val key = args[i].lowercase()
            val valueStr = args.getOrNull(i + 1)
            val value = valueStr?.toDoubleOrNull()

            if (value != null) {
                when (key) {
                    "x" -> cv[0] = value
                    "y" -> cv[1] = value
                    "z" -> cv[2] = value
                    "rx" -> cv[3] = value
                    "ry" -> cv[4] = value
                    "rz" -> cv[5] = value
                }
                i += 2
            } else {
                i++
            }
        }

        api.move.movL(Coordinate(cv[0], cv[1], cv[2], cv[3], cv[4], cv[5]))
    }
}

class MovLogCommand(private val api: DobotE6V4) : Command {
    override val name = "movlog"
    override val description = "直线增量移动"

    override fun execute(args: List<String>) {
        val cv = DoubleArray(6)
        // 获取当前笛卡尔位姿作为基准
        val currentPose = api.calGet.getPose()
        for (i in 0..5)
            cv[i] = currentPose.refValues[i].value as Double

        var i = 0
        while (i < args.size) {
            val key = args[i].lowercase()
            val valueStr = args.getOrNull(i + 1)
            val value = valueStr?.toDoubleOrNull()

            if (value != null) {
                when (key) {
                    "x" -> cv[0] += value // 增量累加
                    "y" -> cv[1] += value
                    "z" -> cv[2] += value
                    "rx" -> cv[3] += value
                    "ry" -> cv[4] += value
                    "rz" -> cv[5] += value
                }
                i += 2
            } else {
                i++
            }
        }
        api.move.movL(Coordinate(cv[0], cv[1], cv[2], cv[3], cv[4], cv[5]))
    }
}