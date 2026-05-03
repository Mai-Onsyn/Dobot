package cius.mai_onsyn.dobot.core.control.console

import cius.mai_onsyn.dobot.core.api.serial.pump.PumpApi
import cius.mai_onsyn.dobot.log

class PumpCommand(
    private val api: PumpApi
): Command {
    override val name = "pump"
    override val description = "Pump commands"

    override fun execute(args: List<String>) {
        when (args[0]) {
            "status" -> log.info(api.status())
            "enable" -> api.enable()
            "disable" -> api.disable()
            "connect" -> log.info("串口初始化：${if (api.connect(args[1])) "success" else "failed"}")
            "close" -> api.disconnect()
            "stop" -> api.stop()
            "speed" -> api.setSpeed(args[1].toInt())
            "accel" -> api.setAccel(args[1].toInt())
            else -> {
                val i = args[0].toIntOrNull()
                if (i != null) {
                    log.info("正在抽水: $i ml")
                    api.pump(i)
                } else log.error("Unknown parameter: ${args[0]}")
            }
        }
    }
}