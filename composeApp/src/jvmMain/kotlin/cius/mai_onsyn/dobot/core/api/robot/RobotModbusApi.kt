package cius.mai_onsyn.dobot.core.api.robot

import cius.mai_onsyn.dobot.core.api.socket.Message
import cius.mai_onsyn.dobot.core.api.socket.RobotConnection
import cius.mai_onsyn.dobot.core.api.socket.RobotResponse

class RobotModbusApi(private val connection: RobotConnection) {
    @JvmOverloads
    fun modbusCreate(
        ip: String,
        port: Int,
        slaveID: Int,
        isRTU: Boolean = false
    ): RobotResponse {
        return connection.send(Message.of("ModbusCreate($ip,$port,$slaveID,${if (isRTU) 1 else 0})"))
    }

    @JvmOverloads
    fun modbusRTUCreate(
        slaveID: Int,
        baud: Int,
        parity: String = "E",
        dataBit: Int = 8,
        stopBit: Int = 1
    ): RobotResponse {
        return connection.send(Message.of("ModbusRTUCreate($slaveID,$baud,$parity,$dataBit,$stopBit)"))
    }

    fun modbusClose(index: Int): RobotResponse =
        connection.send(Message.of("ModbusClose($index)"))

    fun getInBits(index: Int, address: Int, count: Int): RobotResponse =
        connection.send(Message.of("GetInBits($index,$address,$count)"))

    @JvmOverloads
    fun getInRegs(
        index: Int,
        address: Int,
        count: Int,
        valType: String = "U16"
    ): RobotResponse =
        connection.send(Message.of("GetInRegs($index,$address,$count,\"$valType\")"))

    fun getCoils(index: Int, address: Int, count: Int): RobotResponse =
        connection.send(Message.of("GetCoils($index,$address,$count)"))

    fun setCoils(index: Int, address: Int, count: Int, valTab: String): RobotResponse =
        connection.send(Message.of("SetCoils($index,$address,$count,$valTab)"))

    @JvmOverloads
    fun getHoldRegs(
        index: Int,
        address: Int,
        count: Int,
        valType: String = "U16"
    ): RobotResponse =
        connection.send(Message.of("GetHoldRegs(${index.coerceIn(0..4)},$address,$count,\"$valType\")"))

    @JvmOverloads
    fun setHoldRegs(
        index: Int,
        address: Int,
        count: Int,
        valTab: String,
        valType: String = "U16"
    ): RobotResponse =
        connection.send(Message.of("SetHoldRegs(${index.coerceIn(0..4)},$address,$count,$valTab,\"$valType\")"))
}