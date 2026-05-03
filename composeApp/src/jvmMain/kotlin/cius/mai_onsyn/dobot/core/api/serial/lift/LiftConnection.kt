package cius.mai_onsyn.dobot.core.api.serial.lift

import cius.mai_onsyn.dobot.core.api.serial.SerialConnection
import com.fazecast.jSerialComm.SerialPort

class LiftConnection: SerialConnection() {
    fun connect(com: String): Boolean {
        return super.connect(SerialPort.getCommPort(com), 9600)
    }
}