package cius.mai_onsyn.dobot.core.api.robot

import cius.mai_onsyn.dobot.robot.arm.Coordinate
import cius.mai_onsyn.dobot.core.api.socket.Message
import cius.mai_onsyn.dobot.core.api.socket.RobotConnection
import cius.mai_onsyn.dobot.core.api.socket.RobotResponse

class RobotSettingApi(private val connection: RobotConnection) {
    fun speedFactor(value: Int): RobotResponse =
        connection.send(Message.of("SpeedFactor(${value.coerceIn(1..100)})"))

    fun user(index: Int): RobotResponse =
        connection.send(Message.of("User($index)"))

    @JvmOverloads
    fun setUser(index: Int, value: Coordinate, type: Int = 0): RobotResponse =
        connection.send(Message.of("SetUser($index, $value, $type)"))

    fun calcUser(index: Int, matrix: Int, offset: Coordinate): RobotResponse =
        connection.send(Message.of("CalcUser($index, $matrix, $offset)"))

    fun tool(index: Int): RobotResponse =
        connection.send(Message.of("Tool(${index.coerceIn(0..50)})"))

    @JvmOverloads
    fun setTool(index: Int, value: Coordinate, type: Int = 0): RobotResponse =
        connection.send(Message.of("SetTool(${index.coerceIn(1..50)}, $value, $type)"))

    fun calcTool(index: Int, matrix: Int, offset: Coordinate): RobotResponse =
        connection.send(Message.of("CalcTool(${index.coerceIn(0..50)}, $matrix, $offset)"))

    @JvmOverloads
    fun setPayload(load: Double, x: Int = 0, y: Int = 0, z: Int = 0): RobotResponse =
        connection.send(Message.of("SetPayload($load, $x, $y, $z)"))

    fun setPayload(name: String): RobotResponse =
        connection.send(Message.of("SetPayload($name)"))

    fun accJ(r: Int): RobotResponse =
        connection.send(Message.of("AccJ(${r.coerceIn(1..100)})"))

    fun accL(r: Int): RobotResponse =
        connection.send(Message.of("AccL(${r.coerceIn(1..100)})"))

    fun velJ(r: Int): RobotResponse =
        connection.send(Message.of("VelJ(${r.coerceIn(1..100)})"))

    fun velL(r: Int): RobotResponse =
        connection.send(Message.of("VelL(${r.coerceIn(1..100)})"))

    fun cp(r: Int): RobotResponse =
        connection.send(Message.of("CP(${r.coerceIn(0..100)})"))

    fun setCollisionLevel(level: Int): RobotResponse =
        connection.send(Message.of("SetCollisionLevel(${level.coerceIn(0..5)})"))

    fun setBackDistance(distance: Double): RobotResponse =
        connection.send(Message.of("SetBackDistance(${distance.coerceIn(0.0..50.0)})"))

    /**
     * 碰撞后处理方式。
     * 0表示检测到碰撞后进入停止状态，1表示检测到碰撞后进入暂停状态。
     */
    fun setPostCollisionMode(mode: Int): RobotResponse =
        connection.send(Message.of("SetPostCollisionMode(${mode.coerceIn(0..1)})"))

    fun dragSensitivity(index: Int, value: Int): RobotResponse =
        connection.send(Message.of("DragSensivity($index,${value.coerceIn(0..100)})"))

    fun enableSafeSkin(status: Boolean): RobotResponse =
        connection.send(Message.of("EnableSafeSkin(${if (status) 1 else 0})"))

    /**
     * @param part 要设置的部位，3表示arm（小臂安全皮肤），4~6分别表示J4~J6关节。
     * @param status 灵敏度，0表示关闭，1表示low，2表示middle，3表示high。
     */
    fun setSafeSkin(part: Int, status: Int): RobotResponse =
        connection.send(Message.of("SetSafeSkin($part,${status.coerceIn(0..3)})"))

    /**
     * @param index 要设置的安全墙索引，需要先在控制软件中添加对应的安全墙。取值范围：[1,8]。
     * @param value 安全墙开关
     */
    fun setSafeWallEnable(index: Int, value: Boolean): RobotResponse =
        connection.send(Message.of("SetSafeWallEnable($index,${if (value) 1 else 0})"))

    fun setWorkZoneEnable(index: Int, value: Boolean): RobotResponse =
        connection.send(Message.of("SetWorkZoneEnable($index,${if (value) 1 else 0})"))
}