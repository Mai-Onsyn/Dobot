package cius.mai_onsyn.dobot.api.socket

import cius.mai_onsyn.log
import java.io.InputStream
import java.io.PrintWriter
import java.net.Socket

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
        checkConnection()
        val fullCmd = "$cmd;"
        log.debug("Send command: $fullCmd")
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
        log.debug("Received response: ${sb.toString().trim()}")
        return response
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

    private fun checkConnection() {
        if (socket == null || socket?.isClosed == true)
            throw RuntimeException("Socket is not connected")
    }
}