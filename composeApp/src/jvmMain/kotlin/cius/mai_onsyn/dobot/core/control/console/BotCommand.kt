package cius.mai_onsyn.dobot.control.console

import cius.mai_onsyn.dobot.core.api.robot.DobotE6V4
import cius.mai_onsyn.dobot.log

class BotCommand(
    private val api: DobotE6V4
): Command {
    override val name = "bot"
    override val description = "机器人的配置"

    override fun execute(args: List<String>) {
        when (args[0]) {
            "enable" -> api.control.enableRobot()
            "disable" -> api.control.disableRobot()
            "connect" -> api.connect()
            "disconnect" -> api.disconnect()
            "cls" -> api.control.clearError()
            "handc" -> api.hand.connect()
            "handd" -> api.hand.disconnect()
            "setup" -> {
                api.control.clearError()
                val requestControl = api.control.requestControl()
                if (!requestControl.isSuccess()) {
                    api.disconnect()
                    log.error("无法获取控制权")
                }
                api.control.emergencyStop(false)
                api.control.stop()
                api.hand.connect()
            }
            "startdrag" -> api.control.startDrag()
            "stopdrag" -> api.control.stopDrag()
        }
    }
}