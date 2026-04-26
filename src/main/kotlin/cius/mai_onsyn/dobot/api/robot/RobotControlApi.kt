package cius.mai_onsyn.dobot.api.robot

import cius.mai_onsyn.dobot.api.socket.Message
import cius.mai_onsyn.dobot.api.socket.RobotConnection
import cius.mai_onsyn.dobot.api.socket.RobotResponse

class RobotControlApi(private val connection: RobotConnection) {
    fun requestControl(): RobotResponse =
        connection.send(Message.of("RequestControl()"))

    fun powerOn(): RobotResponse =
        connection.send(Message.of("PowerOn()"))

    fun enableRobot(): RobotResponse =
        connection.send(Message.of("EnableRobot()"))

    fun disableRobot(): RobotResponse =
        connection.send(Message.of("DisableRobot()"))

    fun clearError(): RobotResponse =
        connection.send(Message.of("ClearError()"))

    fun runScript(script: String): RobotResponse =
        connection.send(Message.of("RunScript($script)"))

    fun stop(): RobotResponse =
        connection.send(Message.of("Stop()"))

    fun pause(): RobotResponse =
        connection.send(Message.of("Pause()"))

    fun continue_(): RobotResponse =
        connection.send(Message.of("Continue()"))

    fun emergencyStop(stop: Boolean): RobotResponse =
        connection.send(Message.of("EmergencyStop(${if (stop) 1 else 0})"))

    fun brakeControl(): RobotResponse =
        connection.send(Message.of("BrakeControl()"))

    fun startDrag(): RobotResponse =
        connection.send(Message.of("StartDrag()"))

    fun stopDrag(): RobotResponse =
        connection.send(Message.of("StopDrag()"))
}