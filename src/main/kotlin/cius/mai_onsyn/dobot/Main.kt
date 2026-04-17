package cius.mai_onsyn.dobot

import cius.mai_onsyn.dobot.api.DobotE6V4
import cius.mai_onsyn.dobot.api.pump.PumpApi
import cius.mai_onsyn.dobot.api.pump.PumpConnection
import cius.mai_onsyn.dobot.control.console.ConsoleApp

fun main() {
    val dot = DobotE6V4("192.168.5.1")
    val pump = PumpApi(PumpConnection())

    val app = ConsoleApp(dot, pump)
    app.run()
}