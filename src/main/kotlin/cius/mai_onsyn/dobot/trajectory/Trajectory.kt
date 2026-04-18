package cius.mai_onsyn.dobot.trajectory

import cius.mai_onsyn.dobot.api.HandApi
import cius.mai_onsyn.dobot.api.RobotCalGetApi
import cius.mai_onsyn.dobot.api.RobotMoveApi
import cius.mai_onsyn.dobot.robot.arm.RobotState
import com.alibaba.fastjson2.JSONArray
import com.alibaba.fastjson2.JSONObject

interface Trajectory {
    fun record(api: RobotCalGetApi, hand: HandApi): Boolean

    fun replay(api: RobotMoveApi, hand: HandApi)

    fun toJSON(): JSONArray
}