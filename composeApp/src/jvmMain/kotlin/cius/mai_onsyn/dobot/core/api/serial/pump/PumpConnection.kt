package cius.mai_onsyn.dobot.core.api.serial.pump

import cius.mai_onsyn.dobot.core.api.serial.SerialConnection
import cius.mai_onsyn.dobot.log
import com.fazecast.jSerialComm.SerialPort

class PumpConnection(
    val baudRate: Int = 9600
): SerialConnection() {
    fun connect(com: String): Boolean {
        try {
            val bool = super.connect(SerialPort.getCommPort(com), baudRate)
            if (bool) {
                super.writeLine("cnm")
//                        super.writeLine("15")
                val s = super.readLine(1000)
//                        log.debug("Connect Returned: $s")
                if (s == "cnm again") {
                    return true
                } else {
                    super.disconnect()
                }
            }
        } catch (e: Exception) {
            log.error(e)
        }
        return false
    }
}

//fun connect(com: String): Boolean {
//        val serials = SerialPort.getCommPorts()
//        if (serials.isNotEmpty()) {
//            serials.forEach { serialPort ->
//                if (!serialPort.descriptivePortName.contains("USB")) return@forEach
//                try {
//                    val bool = super.connect(serialPort, baudRate)
//                    if (bool) {
//                        super.writeLine("cnm")
////                        super.writeLine("15")
//                        val s = super.readLine(1000)
////                        log.debug("Connect Returned: $s")
//                        if (s == "cnm again") {
//                            return true
//                        } else {
//                            super.disconnect()
//                            return@forEach
//                        }
//                    }
//                } catch (e: Exception) {
//                    log.error(e)
//                }
//            }
//        }
//        return false
//    }