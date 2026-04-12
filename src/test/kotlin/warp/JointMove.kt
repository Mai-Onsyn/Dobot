package warp

import cius.mai_onsyn.dobot.api.DobotE6V4
import cius.mai_onsyn.dobot.robot.arm.Coordinate
import cius.mai_onsyn.dobot.robot.arm.Joint
import cius.mai_onsyn.dobot.robot.arm.RobotError

fun main() {
    val dot = DobotE6V4("192.168.5.1")

    dot.connect()
    val requestControl = dot.control.requestControl()
    if (requestControl.refID != RobotError.SUCCESS) {
        dot.disconnect()
        return
    }
    dot.control.emergencyStop(false)
    dot.control.stop()
    dot.control.clearError()
    dot.control.enableRobot()

//    Thread.sleep(3000)
    val p1 = Coordinate(561.0, 0.0, 100.0, -145.0, 0.0, -90.0)
    val p2 = Coordinate(44.0, 651.0, 150.0, -140.0, 0.0, 0.0)
    val j3 = Joint(0.0, 0.0, 0.0, 0.0, 0.0, 0.0)

    val defaultJoint = Joint(0.0, 45.0, -90.0, -45.0, 90.0, 0.0)
//    dot.move.movJ(p1)
//    dot.move.movJ(p2)
//    dot.move.movJ(j3)
    dot.move.movJ(defaultJoint)

//    dot.control.disableRobot()
    dot.disconnect()
}