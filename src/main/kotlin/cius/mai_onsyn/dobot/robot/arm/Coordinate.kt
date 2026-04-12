package cius.mai_onsyn.dobot.robot.arm

class Coordinate(
    val x: Double,
    val y: Double,
    val z: Double,
    val rx: Double,
    val ry: Double,
    val rz: Double
): RobotState {
    override fun toString(): String {
        return "{$x,$y,$z,$rx,$ry,$rz}"
    }

    override fun toStateString(): String {
        return "pose={$x,$y,$z,$rx,$ry,$rz}"
    }
}