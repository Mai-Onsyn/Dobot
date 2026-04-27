package cius.mai_onsyn.dobot.core

import cius.mai_onsyn.dobot.core.api.API
import cius.mai_onsyn.dobot.core.api.pump.PumpApi
import cius.mai_onsyn.dobot.core.api.pump.PumpConnection
import cius.mai_onsyn.dobot.core.api.robot.DobotE6V4

object UIInterface {
    val api = API(
        DobotE6V4("192.168.5.1"),
        PumpApi(PumpConnection())
    )
}