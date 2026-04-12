package cius.mai_onsyn.dobot.robot.arm

/**
 * 机械臂运行模式
 * * 用于标识 Dobot 机械臂当前所处的实时状态。
 * 指令通常返回状态码 [code]
 *
 * @property code 对应协议返回的状态数值
 */
enum class RobotMode(val code: Int) {
    /** 初始化状态 */
    INIT(1),

    /** 有任意关节的抱闸松开 */
    BRAKE_OPEN(2),

    /** 机械臂下电状态 */
    POWEROFF(3),

    /** 未使能（无抱闸松开） */
    DISABLED(4),

    /** 使能且空闲 */
    ENABLE(5),

    /** 拖拽模式（关节拖拽或力控拖拽） */
    BACKDRIVE(6),

    /** 运行状态（工程，TCP 队列运动等） */
    RUNNING(7),

    /** 单次运动状态（点动、RunTo 等） */
    SINGLE_MOVE(8),

    /**
     * 有未清除的报警。
     * 此状态优先级最高，无论机械臂处于什么状态，有报警时都会返回此状态。
     */
    ERROR(9),

    /** 暂停状态 */
    PAUSE(10),

    /** 碰撞检测触发状态 */
    COLLISION(11);

    companion object {
        fun byID(value: Int): RobotMode? = entries.find { it.code == value }
    }
}