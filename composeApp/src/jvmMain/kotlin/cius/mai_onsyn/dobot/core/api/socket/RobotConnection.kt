package cius.mai_onsyn.dobot.core.api.socket

import cius.mai_onsyn.dobot.log
import org.apache.logging.log4j.ThreadContext
import java.io.InputStream
import java.io.PrintWriter
import java.net.Socket
import java.util.concurrent.CompletableFuture
import java.util.concurrent.LinkedBlockingQueue
import kotlin.concurrent.thread

class RobotConnection(
    val host: String,
    val port: Int
) {
    companion object {
        const val READ_TIMEOUT = 15000
    }
    private var socket: Socket? = null
    private var writer: PrintWriter? = null
    private var reader: InputStream? = null

    private val sendQueue = LinkedBlockingQueue<Pair<Message, CompletableFuture<RobotResponse>>>()

    init {
        thread(isDaemon = true, name = "Robot-Send-Worker") {
            while (true) {
                val (cmd, future) = sendQueue.take()
                try {
                    checkConnection()
                    val fullCmd = "$cmd;"

                    if (!fullCmd.startsWith("GetCurrentCommandID"))
                        log.debug("发送: $fullCmd")

                    writer?.print(fullCmd)
                    writer?.flush()

                    val sb = StringBuilder()
                    var byte: Int
                    while (true) {
                        byte = reader?.read() ?: -1
                        if (byte == -1 || byte.toChar() == ';') break
                        sb.append(byte.toChar())
                    }

                    val response = RobotResponse.of(sb.toString().trim())
                    if (!fullCmd.startsWith("GetCurrentCommandID"))
                        log.debug("接收: ${sb.toString().trim()}")
                    future.complete(response)
                } catch (e: Exception) {
                    future.completeExceptionally(e)
                } finally {
                    Thread.sleep(15)
                }
            }
        }
    }

    fun connect(): Boolean {
        socket = Socket(host, port).apply { soTimeout = READ_TIMEOUT }
        writer = socket?.let { PrintWriter(it.getOutputStream(), true) }
        reader = socket?.getInputStream()
        val bool = socket != null && writer != null && reader != null
        if (bool) log.info("Connected to $host:$port")
        else log.error("Failed to connect to $host:$port")
        return bool
    }

    fun send(cmd: Message): RobotResponse {
        val future = CompletableFuture<RobotResponse>()
        sendQueue.put(cmd to future)
        return future.get()
    }

    fun close(): Boolean {
        if (socket == null || socket?.isClosed == true) {
            log.debug("Socket is already closed, or not connected")
            return false
        }
        socket!!.close()
        log.info("Socket closed")
        return true
    }

    fun isConnected(): Boolean = socket?.isConnected == true

    private fun checkConnection() {
        if (socket == null || socket?.isClosed == true)
            throw RuntimeException("Socket is not connected")
    }
}