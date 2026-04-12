package warp

import cius.mai_onsyn.dobot.api.DobotE6V4
import cius.mai_onsyn.dobot.robot.arm.Joint
import cius.mai_onsyn.dobot.robot.hand.HandJoint
import java.util.Scanner

fun main() {
    val dot = DobotE6V4("192.168.5.1")
    if (!dot.initialize()) return

    val hand = dot.handApi

    var percentage = 1.0
    var joint = Joint.DEFAULT

    val scanner = Scanner(System.`in`)
    while (true) {
        val nextLine = scanner.nextLine()
        if (nextLine == "exit") break
        nextLine.toCharArray().forEach {
            when (it) {
                'a' -> percentage -= 0.025
                'd' -> percentage += 0.025
                'q' -> joint = joint.copy(j1 = joint.j1 + 1)
                'w' -> joint = joint.copy(j2 = joint.j2 + 1)
                'e' -> joint = joint.copy(j3 = joint.j3 + 1)
                'r' -> joint = joint.copy(j4 = joint.j4 + 1)
                't' -> joint = joint.copy(j5 = joint.j5 + 1)
                'y' -> joint = joint.copy(j6 = joint.j6 + 1)
                'z' -> joint = joint.copy(j1 = joint.j1 - 1)
                'x' -> joint = joint.copy(j2 = joint.j2 - 1)
                'c' -> joint = joint.copy(j3 = joint.j3 - 1)
                'v' -> joint = joint.copy(j4 = joint.j4 - 1)
                'b' -> joint = joint.copy(j5 = joint.j5 - 1)
                'n' -> joint = joint.copy(j6 = joint.j6 - 1)
                'o' -> dot.control.clearError()
                '1' -> percentage = 1.0
                '2' -> joint = Joint.DEFAULT
            }
            percentage = percentage.coerceIn(0.3..1.0)
            val value = (255 * percentage).toInt()
            hand.setPose(HandJoint(value, 0, value, value, value, value, 255))

            dot.move.movJ(joint)
        }
    }

    hand.setPose(HandJoint.OPEN)
//    hand.setPose(HandJoint(255, 0, 255, 255, 255, 255, 255))
//    hand.setPose(HandJoint(255, 0, 160, 160, 160, 160, 255))
//    dot.move.movJ(Joint(0.0, 45.0, -90.0, -45.0, -90.0, -90.0))
    dot.move.movJ(Joint.DEFAULT)

    println(hand.getPose())
    dot.disconnect()
}