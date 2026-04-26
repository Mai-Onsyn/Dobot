package cius.mai_onsyn.dobot.api.robot

import cius.mai_onsyn.dobot.api.socket.RobotConnection
import org.jetbrains.annotations.TestOnly

class DobotE6V4(host: String) {
    private val dashboard = RobotConnection(host, 29999)
    private val sensor = RobotConnection(host, 30004)

    val control = RobotControlApi(dashboard)
    val setting = RobotSettingApi(dashboard)
    val calGet = RobotCalGetApi(dashboard)
    val move = RobotMoveApi(dashboard)
    val hand = HandApi(RobotModbusApi(dashboard))

    fun connect() {
        dashboard.connect()
//        sensor.connect()
    }

    fun disconnect() {
        hand.disconnect()
        dashboard.close()
//        sensor.close()
    }

    @TestOnly
    fun initialize(): Boolean {
        connect()
        control.clearError()
        val requestControl = control.requestControl()
        if (!requestControl.isSuccess()) {
            disconnect()
            return false
        }
        control.emergencyStop(false)
        control.stop()
        control.enableRobot()
        hand.connect()

        return true
    }
}