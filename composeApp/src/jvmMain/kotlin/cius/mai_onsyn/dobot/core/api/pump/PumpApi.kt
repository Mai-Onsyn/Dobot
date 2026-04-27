package cius.mai_onsyn.dobot.core.api.pump

import cius.mai_onsyn.dobot.log

class PumpApi(
    private val connection: PumpConnection
) {
    fun connect(): Boolean = connection.connect()

    fun disconnect() = connection.disconnect()

    fun status(): String {
        connection.writeLine("status")
        return connection.readLine(1000)?: "ERROR"
    }

    fun enable(): String {
        connection.writeLine("enable")
        return connection.readLine(1000)?: "ERROR"
    }

    fun disable(): String {
        connection.writeLine("disable")
        return connection.readLine(1000)?: "ERROR"
    }

    fun stop() = connection.writeLine("stop")

    fun setSpeed(speed: Int) = connection.writeLine("setmaxspeed $speed")

    fun setAccel(accel: Int) = connection.writeLine("setaccel $accel")

    fun pump(ml: Int) {
        connection.writeLine("$ml")
        Thread.ofVirtual().name("pump").start {
            val readLine = connection.readLine(300_000)
            if (readLine != "The pumping is completed") {
//                throw IllegalStateException()
                log.error("Pumping failed: $readLine")
            } else log.info("已抽水 $ml ml")
        }
    }
}