package cius.mai_onsyn.dobot

import cius.mai_onsyn.dobot.api.API
import cius.mai_onsyn.dobot.api.robot.DobotE6V4
import cius.mai_onsyn.dobot.api.pump.PumpApi
import cius.mai_onsyn.dobot.api.pump.PumpConnection
import cius.mai_onsyn.dobot.control.console.ConsoleApp
import cius.mai_onsyn.dobot.control.jfx.FXApp

fun main() {
    val dot = DobotE6V4("192.168.5.1")
    val pump = PumpApi(PumpConnection())

    val app = ConsoleApp(API(dot, pump))
    app.run()

//    FXApp.launch(API(dot, pump))
}