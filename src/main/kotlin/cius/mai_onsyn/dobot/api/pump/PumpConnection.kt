package cius.mai_onsyn.dobot.api.pump

import com.fazecast.jSerialComm.SerialPort

class PumpConnection(
    val baudRate: Int = 9600
): SerialConnection() {
    fun connect(): Boolean {
        val serials = SerialPort.getCommPorts()
        if (serials.isNotEmpty()) {
            serials.forEach { serialPort ->
                try {
                    val bool = super.connect(serialPort, baudRate)
                    if (bool) {
                        super.writeLine("cnm")
                        Thread.sleep(500)
                        val s = super.readLine(500)
                        if (s == "cnm again") {
                            return true
                        } else return@forEach
                    }
                } catch (e: Exception) {}
            }
        }
        return false
    }
}