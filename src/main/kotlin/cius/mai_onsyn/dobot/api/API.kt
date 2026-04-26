package cius.mai_onsyn.dobot.api

import cius.mai_onsyn.dobot.api.pump.PumpApi
import cius.mai_onsyn.dobot.api.robot.DobotE6V4

data class API(
    val robotApi: DobotE6V4,
    val pumpApi: PumpApi
)
