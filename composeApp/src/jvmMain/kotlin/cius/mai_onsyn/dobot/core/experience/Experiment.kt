package cius.mai_onsyn.dobot.core.experience

class Experiment(): ArrayList<Step>() {
    fun getStepNameList(): List<String> {
        return mapNotNull { if (it.title.startsWith("等待")) null else it.title }
    }

    companion object {
        val METHYLENE_BLUE = Experiment().apply {
            add(Step("移动烧杯", "record replay traj/moveCup"))
            add(Step("等待", "sleep 25"))
            add(Step("拿起玻璃棒", "record replay traj/takeRod"))
            add(Step("等待", "sleep 10"))
            add(Step("蘸取溶液", "record replay traj/dipSolution"))
            add(Step("等待", "sleep 10"))
            add(Step("反复滴定", "titration"))
            add(Step("等待", "sleep 210"))
            add(Step("归位玻璃棒", "record replay traj/rodReset"))
            add(Step("等待", "sleep 10"))
            add(Step("归位烧杯", "record replay traj/moveCup_r"))
            add(Step("等待", "sleep 25"))
            add(Step("分析结果", "sleep 10"))
        }
    }
}