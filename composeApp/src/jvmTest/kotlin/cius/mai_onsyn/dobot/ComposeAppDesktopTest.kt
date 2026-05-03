package cius.mai_onsyn.dobot

import cius.mai_onsyn.dobot.core.api.serial.SerialConnection
import com.fazecast.jSerialComm.SerialPort
import kotlin.test.Test

class ComposeAppDesktopTest {

    @Test
    fun example() {
        val serial = SerialConnection()

        serial.connect(SerialPort.getCommPort("COM5"), 115200)
        serial.writeLine("1")
        Thread.sleep(5000)
    }
}