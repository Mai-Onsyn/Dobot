package cius.mai_onsyn.dobot.robot.arm

import cius.mai_onsyn.dobot.api.toFixedString

class Coordinate(
    val x: Double,
    val y: Double,
    val z: Double,
    val rx: Double,
    val ry: Double,
    val rz: Double
): RobotState {
    override fun toString(): String {
        return "{${x.toFixedString()},${y.toFixedString()},${z.toFixedString()},${rx.toFixedString()},${ry.toFixedString()},${rz.toFixedString()}}"
    }

    override fun toStateString(): String {
        return "pose={${x.toFixedString()},${y.toFixedString()},${z.toFixedString()},${rx.toFixedString()},${ry.toFixedString()},${rz.toFixedString()}}"
    }
}