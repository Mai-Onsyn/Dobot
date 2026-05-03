package cius.mai_onsyn.dobot.core.control.console

import cius.mai_onsyn.dobot.core.api.serial.lift.LiftApi

class LiftCommand(private val api: LiftApi): Command {
    override val name: String = "lift"
    override val description: String = ""

    override fun execute(args: List<String>) {
        if (args.isNotEmpty()) {
            when (args[0]) {
                "up" -> api.stretch()
                "down" -> api.retract()
                "connect" -> api.connect(args[1])
            }
        }
    }
}