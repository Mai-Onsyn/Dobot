/**
 * AI辅助生成
 * DeepSeek-R1
 * 电脑客户端访问
 * 2026年4月2日
 */

import java.io.PrintWriter
import java.net.Socket
import java.util.concurrent.TimeUnit

class DobotE6V4Executor(val ip: String) {
    private val port = 29999

    fun executeJ1Rotation() {
        Socket(ip, port).use { socket ->
            socket.soTimeout = 10000
            val writer = PrintWriter(socket.getOutputStream(), true)
            val reader = socket.getInputStream()

            fun send(cmd: String): String {
                val fullCmd = if (cmd.endsWith(";")) cmd else "$cmd;"
                println(">>> 发送: $fullCmd")
                writer.print(fullCmd)
                writer.flush()

                val sb = StringBuilder()
                var byte: Int
                while (true) {
                    byte = reader.read()
                    if (byte == -1) break
                    val char = byte.toChar()
                    sb.append(char)
                    if (char == ';') break
                }
                val response = sb.toString().trim()
                println("<<< 回复: $response")
                return response
            }

            send("RequestControl()")
            send("Stop()")

            send("ClearError()")

//            send("EnableRobot()")

            println("等待机器人进入空闲状态...")
            TimeUnit.SECONDS.sleep(3)

            send("SpeedFactor(10)")

            //运动
            send("MovJ(joint={45,0,0,0,0,0})")

            TimeUnit.SECONDS.sleep(5)

//            send("DisableRobot()")
        }
    }
}

fun main() {
    DobotE6V4Executor("192.168.5.1").executeJ1Rotation()
}