package cius.mai_onsyn.dobot.core.api

import cius.mai_onsyn.dobot.core.api.serial.pump.PumpApi
import cius.mai_onsyn.dobot.core.api.robot.DobotE6V4
import cius.mai_onsyn.dobot.core.api.serial.lift.LiftApi

data class API(
    val robotApi: DobotE6V4,
    val pumpApi: PumpApi,
    val liftApi: LiftApi
)
