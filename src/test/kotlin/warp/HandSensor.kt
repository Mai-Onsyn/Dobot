package warp

import cius.mai_onsyn.dobot.api.DobotE6V4
import cius.mai_onsyn.log
import java.util.Scanner
import kotlin.concurrent.thread

fun main() {
    val dot = DobotE6V4("192.168.5.1")
    dot.initialize()

    val handApi = dot.hand

    var appExited = false

    thread {
        while (!appExited) {
            log.info("=".repeat(80))
            log.info("Pose:         ${handApi.getPose()?.toString()}")
            Thread.sleep(15)
            log.info("Torque:       ${handApi.getTorque()?.toString()}")
            Thread.sleep(15)
            log.info("Speed:        ${handApi.getSpeed()?.toString()}")
            Thread.sleep(15)
            log.info("Temperature:  ${handApi.getTemperature()?.toString()}")
            Thread.sleep(15)
            log.info("Normal Force: ${handApi.getNormalForce()?.toString()}")
            Thread.sleep(15)
            log.info("Tan F:        ${handApi.getTangentialForce()?.toString()}")
            Thread.sleep(15)
            log.info("Tan F Dir:    ${handApi.getTangentialForceDirection()?.toString()}")
            Thread.sleep(15)
            log.info("Approach Inc: ${handApi.getApproachIncrement()?.toString()}")
            Thread.sleep(1000)
        }
    }

    val scanner = Scanner(System.`in`)
    while (!appExited) {
        val line = scanner.nextLine()
        if (line == "exit") {
            appExited = true
        }
        Thread.sleep(500)
    }
//    dot.control.disableRobot()
    dot.disconnect()
}