package cius.mai_onsyn.dobot.core.api.robot

import cius.mai_onsyn.dobot.core.api.appendUserTool
import cius.mai_onsyn.dobot.core.robot.arm.Coordinate
import cius.mai_onsyn.dobot.core.robot.arm.Joint
import cius.mai_onsyn.dobot.core.robot.arm.RobotState
import cius.mai_onsyn.dobot.core.api.socket.Message
import cius.mai_onsyn.dobot.core.api.socket.RobotConnection
import cius.mai_onsyn.dobot.core.api.socket.RobotResponse

class RobotMoveApi(private val connection: RobotConnection) {
    private var currentCmdID = -1
    init {
        Thread.ofVirtual().name("MoveCommandAsker").start {
            while (true) {
                try {
                    if (connection.isConnected()) getCurrentCommandID()?.let { id ->
                        if (id != currentCmdID) {
                            currentCmdID = id
                        }
                    }
                } catch (e: Exception) {
//                    e.printStackTrace()
                } finally {
                    Thread.sleep(200)
                }
            }
        }
    }

    @JvmOverloads
    fun movJ(
        state: RobotState,
        user: Int = -1, tool: Int = -1,
        a: Int = -1,
        v: Int = -1,
        cp: Int = -1,
        block: Boolean = false
    ): RobotResponse {
        val sb = StringBuilder("MovJ(")
            .append(state.toStateString())
            .appendUserTool(user, tool)
            .appendValue(a, "a", 1..100)
            .appendValue(v, "v", 1..100)
            .appendValue(cp, "cp", 0..100)
            .append(")")
        val response = connection.send(Message.of(sb.toString()))
        while (
            response.refValues.first().value as Int > currentCmdID &&
            block
        ) {
            Thread.sleep(200)
        }
        return response
    }

    @JvmOverloads
    fun movL(
        state: RobotState,
        user: Int = -1, tool: Int = -1,
        a: Int = -1,
        v: Int = -1,
        speed: Int = -1,
        cp: Int = -1,
        r: Int = -1,
    ): RobotResponse {
        val sb = StringBuilder("MovL(")
            .append(state.toStateString())
            .appendUserTool(user, tool)
            .appendValue(a, "a", 1..100)
            .appendValue(v, "v", 1..100)
            .appendValue(speed, "speed")
            .appendValue(cp, "cp", 0..100)
            .appendValue(r, "r", 0..100)
            .append(")")
        return connection.send(Message.of(sb.toString()))
    }

    fun movJIO() {
        //TODO("Not yet implemented")
    }

    fun movLIO() {
        //TODO("Not yet implemented")
    }

    @JvmOverloads
    fun arc(
        p1: RobotState,
        p2: RobotState,
        user: Int = -1, tool: Int = -1,
        a: Int = -1,
        v: Int = -1,
        speed: Int = -1,
        cp: Int = -1,
        r: Int = -1,
        mode: Int = -1
    ): RobotResponse {
        val sb = StringBuilder("MovL(")
            .append(p1.toStateString())
            .append(p2.toStateString())
            .appendUserTool(user, tool)
            .appendValue(a, "a", 1..100)
            .appendValue(v, "v", 1..100)
            .appendValue(speed, "speed")
            .appendValue(cp, "cp", 0..100)
            .appendValue(r, "r")
            .appendValue(mode, "mode", 0..2)
            .append(")")
        return connection.send(Message.of(sb.toString()))
    }

    fun arcIO() {
        //TODO("Not yet implemented")
    }

    @JvmOverloads
    fun circle(
        p1: RobotState,
        p2: RobotState,
        count: Int = 1,
        user: Int = -1, tool: Int = -1,
        a: Int = -1,
        v: Int = -1,
        speed: Int = -1,
        cp: Int = -1,
        r: Int = -1,
        mode: Int = -1
    ): RobotResponse {
        val sb = StringBuilder("MovC(")
            .append(p1.toStateString())
            .append(p2.toStateString())
            .appendUserTool(user, tool)
            .appendValue(count, "count")
            .appendValue(a, "a", 1..100)
            .appendValue(v, "v", 1..100)
            .appendValue(speed, "speed")
            .appendValue(cp, "cp", 0..100)
            .appendValue(r, "r")
            .appendValue(mode, "mode", 0..2)
            .append(")")
        return connection.send(Message.of(sb.toString()))
    }

    @JvmOverloads
    fun servoJ(
        joint: Joint,
        t: Float = 0.1f,
        aheadTime: Float = 50f,
        gain: Float = 500f
    ): RobotResponse =
        connection.send(Message.of("ServoJ(${joint.j1},${joint.j2},${joint.j3},${joint.j4},${joint.j5},${joint.j6},$t,$aheadTime,$gain)"))

    @JvmOverloads
    fun servoP(
        coo: Coordinate,
        t: Float = 0.1f,
        aheadTime: Float = 50f,
        gain: Float = 500f
    ): RobotResponse =
        connection.send(Message.of("ServoP(${coo.x},${coo.y},${coo.z},${coo.rx},${coo.ry},${coo.rz},${t},${aheadTime},${gain})"))

    @JvmOverloads
    fun moveJog(
        axis: String,
        coordType: Int = -1,
        user: Int, tool: Int
    ): RobotResponse {
        val sb = StringBuilder("MoveJog($axis")
            .appendValue(coordType, "coord_type", 0..2)
            .appendUserTool(user, tool)
            .append(")")
        return connection.send(Message.of(sb.toString()))
    }

    @JvmOverloads
    fun runTo(
        state: RobotState,
        moveType: Int = 1,
        user: Int, tool: Int,
        a: Int,
        v: Int
    ): RobotResponse {
        val sb = StringBuilder("RunTo(")
            .append(state.toStateString())
            .appendValue(moveType, "move_type", 0..4)
            .appendUserTool(user, tool)
            .appendValue(a, "a", 1..100)
            .appendValue(v, "v", 1..100)
            .append(")")
        return connection.send(Message.of(sb.toString()))
    }

    private fun StringBuilder.appendValue(value: Int, tag: String, range: ClosedRange<Int>? = null): StringBuilder {
        if (value != -1) this
            .append(",")
            .append(tag)
            .append("=")
            .append(value.coerceIn(range?: Int.MIN_VALUE..Int.MAX_VALUE))
        return this
    }

    private fun getCurrentCommandID(): Int? {
        val response = connection.send(Message.of("GetCurrentCommandID()"))
        return try {
            response.refValues.first().value as Int
        } catch (_: NoSuchElementException) {
            null
        }
    }
}