/**
 * AI辅助生成
 * DeepSeek-R1
 * 电脑客户端访问
 * 2026年4月3日
 */
import java.io.DataInputStream
import java.net.Socket
import java.nio.ByteBuffer
import java.nio.ByteOrder

fun main() {
    val ip = "192.168.5.1"
    val port = 30004 // 反馈端口

    try {
        val socket = Socket(ip, port)
        val inputStream = DataInputStream(socket.getInputStream())
        val buffer = ByteArray(1440) // E6 标准反馈包大小

        println("正在监听机器人状态 (Ctrl+C 退出)...")

        while (true) {
            inputStream.readFully(buffer) // 阻塞读取完整的一包数据

            // 使用 ByteBuffer 处理小端序数据
            val byteBuffer = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN)

            // 提取 X, Y, Z (参考 E6 最新协议文档的 Offset)
            // 注意：不同固件版本的 Offset 可能有微调，建议查阅配套 PDF
            val x = byteBuffer.getDouble(56)
            val y = byteBuffer.getDouble(64)
            val z = byteBuffer.getDouble(72)
            val mode = byteBuffer.getDouble(232) // 机器人当前模式

            println("坐标: X=$x, Y=$y, Z=$z | 状态码: $mode")

            Thread.sleep(100) // 采样率控制
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}