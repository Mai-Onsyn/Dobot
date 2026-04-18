package cius.mai_onsyn.dobot.control.console

import cius.mai_onsyn.dobot.api.DobotE6V4
import cius.mai_onsyn.dobot.robot.arm.Joint
import cius.mai_onsyn.dobot.robot.hand.HandJoint

class ResumeCommand(
    val api: DobotE6V4
): Command {
    override val name = "resume"
    override val description = "恢复某个东西到指定位置"

    override fun execute(args: List<String>) {
        when (args[0]) {
            "init" -> {
                api.move.movJ(Joint.DEFAULT)
                api.hand.setPose(HandJoint.OPEN)
            }
            "pose" -> {
                api.move.movL(Joint.OUT_FACTORY_FLIP)
                api.hand.setPose(HandJoint.FRIENDLY_POSE)
            }
        }
    }
}