package cius.mai_onsyn.dobot.trajectory

import cius.mai_onsyn.dobot.api.RobotCalGetApi
import com.github.kwhat.jnativehook.GlobalScreen
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener
import java.util.concurrent.atomic.AtomicBoolean
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.concurrent.thread
import kotlin.system.exitProcess

class TrajectoryManager(
    private val savePath: String,
    private val api: RobotCalGetApi, // 你提供的 API 接口
    private val samplingIntervalMs: Long = 50 // 采样间隔，例如 20Hz
) : NativeKeyListener {

    private var currentTrajectory: JointTrajectory? = null
    private val isRecording = AtomicBoolean(false)

    // 用于控制录制线程的生命周期
    private var workerThread: Thread? = null

    init {
        // 禁用 JNativeHook 默认的冗余日志
        val logger = Logger.getLogger(GlobalScreen::class.java.`package`.name)
        logger.level = Level.OFF
        logger.useParentHandlers = false
    }

    /**
     * 启动监听
     */
    fun startListening() {
        GlobalScreen.registerNativeHook()
        GlobalScreen.addNativeKeyListener(this)
        println(">>> 脚本已启动。按下 [F10] 开始/停止记录。")
    }

    /**
     * 停止监听并释放资源
     */
    fun stopListening() {
        stopRecording()
        GlobalScreen.removeNativeKeyListener(this)
        GlobalScreen.unregisterNativeHook()
    }

    private fun startRecording() {
        if (isRecording.compareAndSet(false, true)) {
            currentTrajectory = JointTrajectory()
            println("● 开始记录...")

            workerThread = thread(start = true, isDaemon = true) {
                try {
                    while (isRecording.get()) {
                        currentTrajectory?.record(api)

                        Thread.sleep(samplingIntervalMs)
                    }
                } catch (e: InterruptedException) {
                    // 线程停止
                }
            }
        }
    }

    private fun stopRecording() {
        if (isRecording.compareAndSet(true, false)) {
            workerThread?.interrupt()
            workerThread = null

            println("■ 停止记录。正在保存到: $savePath")
            currentTrajectory?.write(savePath)
            currentTrajectory = null
        }
    }

    override fun nativeKeyPressed(e: NativeKeyEvent) {
        if (e.keyCode == NativeKeyEvent.VC_F10) {
            if (!isRecording.get()) {
                startRecording()
            } else {
                stopRecording()
            }
        }

        if (e.keyCode == NativeKeyEvent.VC_ESCAPE) {
            stopListening()
            exitProcess(0)
        }
    }

    override fun nativeKeyReleased(e: NativeKeyEvent) {}
    override fun nativeKeyTyped(e: NativeKeyEvent) {}
}