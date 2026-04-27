package cius.mai_onsyn.dobot.robot.arm

enum class RobotError(val id: Int) {
    SUCCESS(0),
    UNKNOWN_COMMAND(-10000),
    UNKNOWN_ERROR(Int.MIN_VALUE);

    companion object {
        fun byID(id: Int): RobotError {
            return entries.firstOrNull { it.id == id } ?: UNKNOWN_ERROR
        }
    }
}