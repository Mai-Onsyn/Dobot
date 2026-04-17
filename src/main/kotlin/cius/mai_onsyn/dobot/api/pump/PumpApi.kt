package cius.mai_onsyn.dobot.api.pump

class PumpApi(
    private val connection: PumpConnection
) {
    fun connect(): Boolean = connection.connect()

    fun disconnect() = connection.disconnect()

    fun status(): String {
        connection.writeLine("status")
        return connection.readLine(1000)?: "ERROR"
    }

    fun enable() = connection.writeLine("enable")

    fun disable() = connection.writeLine("disable")

    fun stop() = connection.writeLine("stop")

    fun setSpeed(speed: Int) = connection.writeLine("setmaxspeed $speed")

    fun setAccel(accel: Int) = connection.writeLine("setaccel $accel")

    fun pump(ml: Int) {
        connection.writeLine("$ml")
        val readLine = connection.readLine(300_000)
        if (readLine != "The pumping is completed") {
            throw IllegalStateException("Pumping failed")
        }
    }
}