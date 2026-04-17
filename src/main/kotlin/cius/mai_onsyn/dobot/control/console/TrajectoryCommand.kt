package cius.mai_onsyn.dobot.control.console

import cius.mai_onsyn.dobot.api.DobotE6V4
import cius.mai_onsyn.dobot.trajectory.JointTrajectory
import cius.mai_onsyn.log
import java.io.File


private val trajectory = JointTrajectory()
private val filePath = System.getProperty("user.dir") + "/"

class RecordCommand(
    private val api: DobotE6V4
): Command {
    override val name = "record"
    override val description = "记录当前关节点"

    override fun execute(args: List<String>) {
        if (args.isEmpty()) trajectory.record(api.calGet)
        else if (args[0].lowercase() == "clear") trajectory.clear()
        else if (args.size == 2 && args[0].lowercase() == "save") {
            val filename = args[1].lowercase().removeSuffix("\"").removePrefix("\"")
            trajectory.write("$filePath$filename.json")
        }
        else if (args.size == 2 && args[0].lowercase() == "replay") {
            val filename = args[1].lowercase().removeSuffix("\"").removePrefix("\"")
            val pathname = "$filePath$filename.json"
            if (File(pathname).exists()) {
                JointTrajectory.load(pathname).replay(api.move)
            } else log.error("文件不存在: $pathname")
        }
        else log.error("参数错误")
    }
}