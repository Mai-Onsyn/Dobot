package cius.mai_onsyn.dobot.trajectory

import cius.mai_onsyn.dobot.api.robot.HandApi
import cius.mai_onsyn.dobot.api.robot.RobotCalGetApi
import cius.mai_onsyn.dobot.api.robot.RobotMoveApi
import com.alibaba.fastjson2.JSONArray

interface Trajectory {
    fun record(api: RobotCalGetApi, hand: HandApi): Boolean

    fun replay(api: RobotMoveApi, hand: HandApi)

    fun toJSON(): JSONArray
}