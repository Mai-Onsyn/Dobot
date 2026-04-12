package cius.mai_onsyn.dobot.trajectory

import cius.mai_onsyn.dobot.api.RobotCalGetApi
import cius.mai_onsyn.dobot.api.RobotMoveApi
import cius.mai_onsyn.dobot.robot.arm.RobotState
import com.alibaba.fastjson2.JSONArray
import com.alibaba.fastjson2.JSONObject

interface Trajectory {
    fun record(api: RobotCalGetApi)

    fun replay(api: RobotMoveApi)

    fun toJSON(): JSONArray
}