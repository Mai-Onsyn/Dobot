package cius.mai_onsyn.dobot.api.pump

import cius.mai_onsyn.log
import com.fazecast.jSerialComm.SerialPort
import com.fazecast.jSerialComm.SerialPortDataListener
import com.fazecast.jSerialComm.SerialPortEvent
import java.util.*

open class SerialConnection {
    companion object {
        const val BAUD_RATE = 9600
        const val READ_TIMEOUT = 15000
    }

    private var comPort: SerialPort? = null

    fun connect(port: SerialPort, baudRate: Int): Boolean {
        comPort = port
        comPort!!.baudRate = baudRate
        comPort!!.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, READ_TIMEOUT, 0)

        if (comPort!!.openPort()) {
            log.info("成功连接到端口: ${port.systemPortName}")
            return true
        } else {
            log.error("无法打开端口: ${port.systemPortName}")
            return false
        }
    }

    fun writeLine(text: String?) {
        if (comPort != null && comPort!!.isOpen) {
            val data: ByteArray = (text + "\n").toByteArray()
            comPort!!.writeBytes(data, data.size)
        } else throw IllegalStateException("未开启端口：$${comPort?.systemPortName}")
    }

    fun readLine(timeoutMillis: Int = READ_TIMEOUT): String? {
        if (comPort == null || !comPort!!.isOpen) return null

        setReadTimeout(timeoutMillis)
        val scanner = Scanner(comPort!!.inputStream)
        try {
            if (scanner.hasNextLine()) {
                return scanner.nextLine().trim()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
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
            comPort!!.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, timeoutMillis, 0)
        }
    }

    fun disconnect() {
        if (comPort != null && comPort!!.isOpen) {
            comPort!!.closePort()
            log.info("串口已关闭")
        }
    }
}