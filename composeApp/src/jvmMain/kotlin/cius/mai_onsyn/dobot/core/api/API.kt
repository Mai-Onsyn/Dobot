package cius.mai_onsyn.dobot.core.api

import cius.mai_onsyn.dobot.core.api.pump.PumpApi
import cius.mai_onsyn.dobot.core.api.robot.DobotE6V4

data class API(
    val robotApi: DobotE6V4,
    val pumpApi: PumpApi
)
