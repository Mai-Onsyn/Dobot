package cius.mai_onsyn.dobot.core.control.console

import cius.mai_onsyn.dobot.core.api.robot.DobotE6V4
import cius.mai_onsyn.dobot.core.trajectory.JointTrajectory
import cius.mai_onsyn.dobot.log
import java.io.File


private val trajectory = JointTrajectory()
private val filePath = System.getProperty("user.dir") + "/"

class RecordCommand(
    private val api: DobotE6V4
): Command {
    override val name = "record"
    override val description = "记录当前关节点"

    override fun execute(args: List<String>) {
        if (args.isEmpty()) {
            if (trajectory.record(api.calGet, api.hand))
                log.info("已记录当前点")
        }
        else if (args[0].lowercase() == "clear") {
            val count = trajectory.size
            trajectory.clear()
            log.info("已清空 $count 个轨迹点")
        }
        else if (args[0].lowercase() == "list") {
            log.info("当前${trajectory.size}个轨迹点：")
            trajectory.forEachIndexed { index, (joint, pose) ->
                log.info("$index: $joint, $pose")
            }
        }
        else if (args.size == 2 && args[0].lowercase() == "save") {
            val filename = args[1].lowercase().removeSuffix("\"").removePrefix("\"")
            trajectory.write("$filePath$filename.json")
            log.info("已保存轨迹文件: $filename.json")
        }
        else if (args.size == 2 && args[0].lowercase() == "replay") {
            val filename = args[1].lowercase().removeSuffix("\"").removePrefix("\"")
            val pathname = "$filePath$filename.json"
            if (File(pathname).exists()) {
                log.info("运行轨迹文件: $filename.json")
                JointTrajectory.load(pathname).replay(api.move, api.hand)
            } else log.error("文件不存在: $pathname")
        }
        else if (args.size == 2 && args[0].lowercase() == "load") {
            val filename = args[1].lowercase().removeSuffix("\"").removePrefix("\"")
            val pathname = "$filePath$filename.json"
            if (File(pathname).exists()) {
                log.info("加载轨迹文件: $filename.json")
                trajectory.addAll(JointTrajectory.load(pathname))
                log.info("已从${pathname}加载${trajectory.size}个点记录")
            }
        }
        else if (args.size == 2 && args[0].lowercase() == "move") {
            val index = args[1].toIntOrNull()
            if (index != null && index in 0 until trajectory.size) {
                val point = trajectory[index]
                api.move.movJ(point.first)
                api.hand.setPose(point.second)
            }
        }
        else if (args[0].lowercase() == "rml") {
            if (args.size == 1) trajectory.removeLast()
            else if (args.size == 2) {
                args[1].toIntOrNull()?.let { trajectory.removeAt(it) }
            }
        }
        else log.error("参数错误")
    }
}