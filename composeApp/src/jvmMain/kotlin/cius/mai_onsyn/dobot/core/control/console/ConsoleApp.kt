package cius.mai_onsyn.dobot.core.control.console

import cius.mai_onsyn.dobot.core.api.API
import cius.mai_onsyn.dobot.log
import java.util.Scanner

class ConsoleApp(
    val api: API
) {

    private val commands = mapOf(
        "finger" to FingerCommand(api.robotApi),
        "hand" to HandCommand(api.robotApi),
        "pump" to PumpCommand(api.pumpApi),
        "movj" to MovJCommand(api.robotApi),
        "movjog" to MovJogCommand(api.robotApi),
        "movl" to MovLCommand(api.robotApi),
        "movlog" to MovLogCommand(api.robotApi),
        "record" to RecordCommand(api.robotApi),
        "bot" to BotCommand(api.robotApi),
        "reset" to ResetCommand(api.robotApi),
        "sleep" to SleepCommand(),
        "titration" to ExperimentDropCommand(api, this),
        "lift" to LiftCommand(api.liftApi)
    )

    fun run() {
        val scanner = Scanner(System.`in`)
        log.info("DobotE6V4控制台已启动")
        log.info("输入 'exit' 退出")
//        commands["bot"]?.execute(listOf("connect"))
//        commands["bot"]?.execute(listOf("setup"))
        while (true) {
            if (scanner.hasNextLine()) {
                val line = scanner.nextLine().split(" ")

                if (line[0] == "exit") {
                    try {
                        api.robotApi.hand.disconnect()
                        api.robotApi.disconnect()
                        api.pumpApi.disconnect()
                    } catch (_: Exception) {}
                    break
                }

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

    fun executeLine(lineInput: String) {
        if (lineInput.isBlank()) return
        if (!lineInput.startsWith("sleep")) log.info("正在执行命令：$lineInput")

        if (lineInput.startsWith("sleep")) {
            val parts = lineInput.trim().split("\\s+".toRegex())
            val cmdName = parts[0]
            val args = parts.drop(1)

            val command = commands[cmdName]
            if (command == null) {
                log.error("未知命令: $cmdName")
                return
            }

            try {
                command.execute(args)
            } catch (e: Exception) {
                log.error("命令执行失败: ${e.message}")
            }
        }
    }
}