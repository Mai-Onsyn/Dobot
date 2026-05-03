package cius.mai_onsyn.dobot.core.api.serial.lift

class LiftApi(private val connection: LiftConnection) {
    fun connect(com: String): Boolean = connection.connect(com)
    fun disconnect() = connection.disconnect()

    fun stretch() = connection.writeLine("1")
    fun retract() = connection.writeLine("2")
}