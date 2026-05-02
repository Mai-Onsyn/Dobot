package cius.mai_onsyn.dobot.core.api.socket

import cius.mai_onsyn.dobot.core.api.splitTopLevel
import cius.mai_onsyn.dobot.core.robot.arm.RobotError

data class RobotResponse(
    val refID: RobotError,
    val refValues: List<ParamValue>,
    val command: Message
) {
    companion object {
        fun of(refStr: String): RobotResponse {
            val refStrList = splitTopLevel(refStr)
            if (refStrList.size != 3) {
                throw IllegalArgumentException("Invalid response format: ${refStrList.joinToString(",")}")
            }
            val errorID = RobotError.byID(refStrList[0].toIntOrNull()?: Int.MIN_VALUE)
            val refValues = refStrList[1].substring(1, refStrList[1].length - 1)
                .split(',').map { ParamValue.of(it.trim()) }
            val command = Message.of(refStrList[2])

            return RobotResponse(errorID, refValues, command)
        }
    }
    fun isSuccess() = refID == RobotError.SUCCESS

    override fun toString(): String {
        return "${refID.id},${refValues.joinToString(",", "{", "}")},$command"
    }
}