package warp

import cius.mai_onsyn.dobot.api.DobotE6V4
import cius.mai_onsyn.dobot.robot.arm.Joint
import cius.mai_onsyn.dobot.robot.hand.HandJoint

fun main() {
    val dot = DobotE6V4("192.168.5.1")
    if (!dot.initialize()) return

    val hand = dot.handApi

    hand.setPose(HandJoint.FRIENDLY_POSE)
//    hand.setPose(HandJoint(255, 0, 255, 255, 255, 255, 255))
//    hand.setPose(HandJoint(255, 0, 160, 160, 160, 160, 255))
    dot.move.movJ(Joint(0.0, 45.0, -90.0, -45.0, -90.0, -90.0))

    println(hand.getPose())
    dot.disconnect()
}