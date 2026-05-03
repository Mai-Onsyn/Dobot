package cius.mai_onsyn.dobot.core

import cius.mai_onsyn.dobot.core.api.API
import cius.mai_onsyn.dobot.core.api.serial.pump.PumpApi
import cius.mai_onsyn.dobot.core.api.serial.pump.PumpConnection
import cius.mai_onsyn.dobot.core.api.robot.DobotE6V4
import cius.mai_onsyn.dobot.core.api.serial.lift.LiftApi
import cius.mai_onsyn.dobot.core.api.serial.lift.LiftConnection
import cius.mai_onsyn.dobot.core.control.console.ConsoleApp

object UIInterface {
    val api = API(
        DobotE6V4("192.168.5.1"),
        PumpApi(PumpConnection()),
        LiftApi(LiftConnection())
    )
    val app = ConsoleApp(api)
}