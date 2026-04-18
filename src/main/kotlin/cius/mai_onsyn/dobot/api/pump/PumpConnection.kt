package cius.mai_onsyn.dobot.api.pump

import cius.mai_onsyn.log
import com.fazecast.jSerialComm.SerialPort

class PumpConnection(
    val baudRate: Int = 9600
): SerialConnection() {
    fun connect(): Boolean {
        val serials = SerialPort.getCommPorts()
        if (serials.isNotEmpty()) {
            serials.forEach { serialPort ->
                if (!serialPort.descriptivePortName.contains("USB")) return@forEach
                try {
                    val bool = super.connect(serialPort, baudRate)
                    if (bool) {
                        super.writeLine("cnm")
//                        super.writeLine("15")
                        val s = super.readLine(1000)
//                        log.debug("Connect Returned: $s")
                        if (s == "cnm again") {
                            return true
                        } else {
                            super.disconnect()
                            return@forEach
                        }
                    }
                } catch (e: Exception) {
                    log.error(e)
                }
            }
        }
        return false
    }
}