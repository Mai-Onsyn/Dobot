package cius.mai_onsyn.dobot.core.control.console

import cius.mai_onsyn.dobot.core.api.API
import cius.mai_onsyn.dobot.log

class SleepCommand: Command {
    override val name: String = "sleep"
    override val description: String = "Sleeps for a given number of seconds"

    override fun execute(args: List<String>) {
        if (args.size != 1) {
            log.error("Usage: $name <seconds>")
        }
        val seconds = args[0].toDoubleOrNull() ?: throw IllegalArgumentException("Invalid number of seconds")
        Thread.sleep((seconds * 1000).toLong())
    }
}

class ExperimentDropCommand(
    private val api: API,
    private val app: ConsoleApp
): Command {
    override val name: String = "titration"
    override val description: String = "仅用于亚加蓝滴定实验"

    override fun execute(args: List<String>) {
        for (i in 0..3) {
            dropOnce(i)
            log.info("等待30秒")
            Thread.sleep(3000)
        }
    }

    private val dropPoints = listOf(
        "drop1", "drop3", "drop4", "drop6"
    )
    fun dropOnce(index: Int) {
        if (index != 0) app.executeLine("record replay traj/rodRepeat_r")
        app.executeLine("record replay traj/rodRepeat")
        app.executeLine("record replay traj/${dropPoints[index]}")
        app.executeLine("record replay traj/drope")
    }

}