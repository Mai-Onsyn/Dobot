package cius.mai_onsyn.dobot.core.api.serial

import cius.mai_onsyn.dobot.log
import com.fazecast.jSerialComm.SerialPort
import com.fazecast.jSerialComm.SerialPortDataListener
import com.fazecast.jSerialComm.SerialPortEvent
import java.util.Scanner

open class SerialConnection {
    companion object {
        const val BAUD_RATE = 9600
        const val WRITE_TIMEOUT = 1000
        const val READ_TIMEOUT = 15000
    }

    private var comPort: SerialPort? = null

    fun connect(port: SerialPort, baudRate: Int): Boolean {
        comPort = port
        comPort!!.setComPortParameters(baudRate, 8, 1, 0)
        comPort!!.setFlowControl(SerialPort.FLOW_CONTROL_DISABLED)
        comPort!!.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, READ_TIMEOUT, WRITE_TIMEOUT)
        comPort!!.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, READ_TIMEOUT, WRITE_TIMEOUT)

        if (comPort!!.openPort()) {
            comPort!!.setDTR()
            comPort!!.setRTS()
            Thread.sleep(2000)
            log.info("成功连接到端口: ${port.systemPortName}")
            comPort!!.flushIOBuffers()
            return true
        } else {
            log.error("无法打开端口: ${port.systemPortName}")
            return false
        }
    }

    fun writeLine(text: String) {
        if (comPort != null && comPort!!.isOpen) {
            val data: ByteArray = "$text\n".toByteArray()
//            comPort!!.flushIOBuffers()
            comPort!!.writeBytes(data, data.size)
        } else throw IllegalStateException("未开启端口：$${comPort?.systemPortName}")
    }

    private val readBuffer = ByteArray(64)
    fun readLine(timeoutMillis: Int = READ_TIMEOUT): String? {
        if (comPort == null || !comPort!!.isOpen) return null

//        setReadTimeout(timeoutMillis)
        val lastReadTime = System.currentTimeMillis()
        while (comPort!!.bytesAvailable() == 0) {
            if (timeoutMillis > 0 && System.currentTimeMillis() - lastReadTime > timeoutMillis) {
                log.error("读取超时")
                return null
            } else {
                Thread.sleep(10)
            }
        }
        val scanner = Scanner(comPort!!.inputStream)
        try {
            if (scanner.hasNextLine()) {
                val line = scanner.nextLine().trim()
                log.debug("Serial received: $line")
                return line
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

//        val sb = StringBuilder()
//        while (true) {
//            val read = comPort!!.readBytes(readBuffer, 0, readBuffer.size)
//            if (read > 0) {
//                sb.append(String(readBuffer, 0, read))
//            } else break
//        }

        return null
    }

    fun startListening(listener: (String) -> Unit) {
        comPort!!.addDataListener(object : SerialPortDataListener {
            override fun getListeningEvents(): Int {
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE
            }

            override fun serialEvent(event: SerialPortEvent) {
                if (event.eventType != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) return

                val s = Scanner(comPort!!.inputStream)
                while (s.hasNextLine()) {
                    listener(s.nextLine())
                }
            }
        })
    }

    fun setReadTimeout(timeoutMillis: Int) {
        if (comPort != null) {
            comPort!!.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, timeoutMillis, WRITE_TIMEOUT)
        }
    }

    fun disconnect() {
        if (comPort != null && comPort!!.isOpen) {
            comPort!!.closePort()
            log.info("串口已关闭")
        }
    }
}