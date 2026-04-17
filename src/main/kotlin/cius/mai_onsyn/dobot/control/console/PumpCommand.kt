package cius.mai_onsyn.dobot.control.console

import cius.mai_onsyn.dobot.api.pump.PumpApi
import cius.mai_onsyn.log

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
            "connect" -> log.info(if (api.connect()) "success" else "failed")
            "stop" -> api.stop()
            "speed" -> api.setSpeed(args[1].toInt())
            "accel" -> api.setAccel(args[1].toInt())
            else -> {
                val i = args[0].toIntOrNull()
                if (i != null) {
                    api.pump(i)
                } else log.error("Unknown parameter: ${args[0]}")
            }
        }
    }
}