package cius.mai_onsyn.dobot.api

import cius.mai_onsyn.dobot.robot.arm.Coordinate
import cius.mai_onsyn.dobot.robot.arm.Joint
import cius.mai_onsyn.dobot.robot.arm.RobotMode
import cius.mai_onsyn.dobot.api.socket.Message
import cius.mai_onsyn.dobot.api.socket.RobotConnection
import cius.mai_onsyn.dobot.api.socket.RobotResponse

class RobotCalGetApi(private val connection: RobotConnection) {
    fun robotMode(): RobotMode = RobotMode.byID(
        connection.send(Message.of("RobotMode()"))
            .refValues.first().value as Int
    )!!

    @JvmOverloads
    fun positiveKin(
        joint: Joint,
        user: Int = -1, tool: Int = -1
    ): RobotResponse {
        val sb = StringBuilder("PositiveKin(${joint.j1}, ${joint.j2}, ${joint.j3}, ${joint.j4}, ${joint.j5}, ${joint.j6}")
        sb.appendUserTool(user, tool).append(")")
        return connection.send(Message.of(sb.toString()))
    }

    @JvmOverloads
    fun inverseKin(
        coo: Coordinate,
        useJointNear: Boolean = false,
        jointNear: Coordinate = Coordinate(0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
        user: Int = -1, tool: Int = -1
    ): RobotResponse {
        val sb = StringBuilder("InverseKin(${coo.x}, ${coo.y}, ${coo.z}, ${coo.rx}, ${coo.ry}, ${coo.rz}")
        if (useJointNear) { sb.append(",useJointNear=1,jointNear=").append(jointNear) }
        sb.appendUserTool(user, tool).append(")")
        return connection.send(Message.of(sb.toString()))
    }

    fun getAngle(): RobotResponse =
        connection.send(Message.of("GetAngle()"))

    @JvmOverloads
    fun getPose(
        user: Int = -1, tool: Int = -1
    ): RobotResponse {
        val sb = StringBuilder("GetPose(")
        sb.appendUserTool(user, tool).append(")")
        return connection.send(Message.of(sb.toString()))
    }

    fun getErrorID(): RobotResponse =
        connection.send(Message.of("GetErrorID()"))

    fun createTray(
        trayName: String,
        count: Int,
        p1: Coordinate, p2: Coordinate,
    ) { connection.send(Message.of("CreateTray($trayName,{$count},{${p1.toStateString()},${p2.toStateString()}})")) }

    fun createTray(
        trayName: String,
        row: Int, col: Int,
        p1: Coordinate, p2: Coordinate,
        p3: Coordinate, p4: Coordinate
    ) {
        connection.send(Message.of(
            "CreateTray($trayName,{$row,$col},{${p1.toStateString()},${p2.toStateString()},${p3.toStateString()},${p4.toStateString()}})"
        ))
    }

    fun createTray(
        trayName: String,
        row: Int, col: Int, layer: Int,
        p1: Coordinate, p2: Coordinate,
        p3: Coordinate, p4: Coordinate,
        p5: Coordinate, p6: Coordinate,
        p7: Coordinate, p8: Coordinate
    ) {
        connection.send(Message.of(
            "CreateTray($trayName,{$row,$col,$layer},{${p1.toStateString()},${p2.toStateString()},${p3.toStateString()},${p4.toStateString()},${p5.toStateString()},${p6.toStateString()},${p7.toStateString()},${p8.toStateString()}})"
        ))
    }

    fun getTrayPoint(trayName: String, index: Int): RobotResponse =
        connection.send(Message.of("GetTrayPoint($trayName,$index)"))

    fun getScrName(): RobotResponse =
        connection.send(Message.of("GetScrName()"))
}