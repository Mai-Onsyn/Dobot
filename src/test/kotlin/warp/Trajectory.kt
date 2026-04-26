package warp

import cius.mai_onsyn.dobot.api.robot.DobotE6V4
import cius.mai_onsyn.dobot.robot.arm.Joint
import cius.mai_onsyn.dobot.trajectory.JointTrajectory

fun main() {
    replay()
//    record()
}

fun replay() {
    val dot = DobotE6V4("192.168.5.1")

    if (!dot.initialize()) return

    val trajectory = JointTrajectory.load("D:/Users/Desktop/test1.json")

    dot.move.movJ(Joint.DEFAULT)
//    dot.move.movJ(Joint.of("{75.0785,-205.4027,843.9064,-89.9948,-6.8835,-179.9687}")!!)
    Thread.sleep(10000)

//    trajectory.replay(dot.move)

    dot.disconnect()
}

fun record() {
    val dot = DobotE6V4("192.168.5.1")

    if (!dot.initialize()) return

    val trajectory = TrajectoryManager(
        savePath = "D:/Users/Desktop/test1.json",
        api = dot.calGet,
        samplingIntervalMs = 500
    )

    trajectory.startListening()

    while (true) {
        Thread.sleep(1000)
    }
}