package cius.mai_onsyn.dobot.robot.arm

import cius.mai_onsyn.dobot.core.api.toFixedString

data class Joint(
    val j1: Double,
    val j2: Double,
    val j3: Double,
    val j4: Double,
    val j5: Double,
    val j6: Double,
): RobotState {

    companion object {
        fun of(str: String): Joint? {
            val values = str
                .removePrefix("{")
                .removeSuffix("}")
                .split(",")
                .map { it.toDouble() }
            if (values.size < 6) return null
            return Joint(
                values[0], values[1], values[2],
                values[3], values[4], values[5]
            )
        }

        fun getJointIndex(key: String): Int {
            return when (key) {
                "j1" -> 0
                "j2" -> 1
                "j3" -> 2
                "j4" -> 3
                "j5" -> 4
                "j6" -> 5
                else -> -1
            }
        }

        val DEFAULT = Joint(0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
        val OUT_FACTORY = Joint(0.0, 45.0, -90.0, -45.0, 90.0, 0.0)
        val OUT_FACTORY_FLIP = Joint(0.0, 45.0, -90.0, -45.0, -90.0, -90.0)
    }

    override fun toString(): String {
        return "{${j1.toFixedString()},${j2.toFixedString()},${j3.toFixedString()},${j4.toFixedString()},${j5.toFixedString()},${j6.toFixedString()}}"
    }

    override fun toStateString(): String {
        return "joint={${j1.toFixedString()},${j2.toFixedString()},${j3.toFixedString()},${j4.toFixedString()},${j5.toFixedString()},${j6.toFixedString()}}"
    }

    override fun equals(other: Any?): Boolean {
        if (other as? Joint == null) return false
        return j1 == other.j1 && j2 == other.j2 && j3 == other.j3 &&
               j4 == other.j4 && j5 == other.j5 && j6 == other.j6
    }

    override fun hashCode(): Int {
        var result = j1.hashCode()
        result = 31 * result + j2.hashCode()
        result = 31 * result + j3.hashCode()
        result = 31 * result + j4.hashCode()
        result = 31 * result + j5.hashCode()
        result = 31 * result + j6.hashCode()
        return result
    }
}