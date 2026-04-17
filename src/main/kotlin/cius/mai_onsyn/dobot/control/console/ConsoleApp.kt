package cius.mai_onsyn.dobot.control.console

import cius.mai_onsyn.dobot.api.DobotE6V4
import cius.mai_onsyn.dobot.api.pump.PumpApi
import cius.mai_onsyn.log
import java.util.Scanner

class ConsoleApp(
    val api: DobotE6V4,
    val pumpApi: PumpApi
) {

    private val commands = mapOf(
        "finger" to FingerCommand(api),
        "hand" to HandCommand(api),
        "pump" to PumpCommand(pumpApi),
        "movj" to MovJCommand(api),
        "movjog" to MovJogCommand(api),
    )

    fun run() {
        val scanner = Scanner(System.`in`)
        log.info("DobotE6V4控制台已启动")
        log.info("输入 'exit' 退出")
        while (true) {
            if (scanner.hasNextLine()) {
                val line = scanner.nextLine().split(" ")

                if (line[0] == "exit") break

                val command = commands[line[0]]
                if (command == null) log.error("未知命令: ${line[0]}")

                try {
                    command?.execute(line.drop(1))
                } catch (e: Exception) {
                    log.error("命令执行失败: ${e.message}")
                }
            }
            Thread.sleep(200)
        }
    }
}