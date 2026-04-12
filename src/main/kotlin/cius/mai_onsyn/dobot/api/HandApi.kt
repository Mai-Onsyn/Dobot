package cius.mai_onsyn.dobot.api

import cius.mai_onsyn.dobot.robot.hand.HandJoint
import cius.mai_onsyn.dobot.robot.hand.HandFinger

class HandApi(private val modbus: RobotModbusApi) {
    private var modbusIndex: Int = -1   //Modbus主站索引
    private val slaveId = 39            //右手

    companion object {
        fun disconnectAll(modbusApi: RobotModbusApi) {
            for (i in 0..4) {
                modbusApi.modbusClose(i)
            }
        }
    }

    fun connect(): Boolean {
        val response = modbus.modbusCreate("192.168.5.1", 60000, slaveId, true)
        if (response.isSuccess()) {
            modbusIndex = response.refValues.first().value as Int
            return true
        }
        return false
    }

    fun disconnect(): Boolean = modbus.modbusClose(modbusIndex).isSuccess()

    fun setPose(pose: HandJoint): Boolean = universalHandJointSetter(0, pose)
    fun setTorque(torque: HandJoint): Boolean = universalHandJointSetter(7, torque)
    fun setSpeed(speed: HandJoint): Boolean = universalHandJointSetter(14, speed)
    fun setLockRotorThreshold(threshold: HandJoint): Boolean = universalHandJointSetter(21, threshold)
    fun setLockRotorTime(time: HandJoint): Boolean = universalHandJointSetter(28, time)
    fun setLockRotorTorque(torque: HandJoint): Boolean = universalHandJointSetter(35, torque)

    fun getPose(): HandJoint? = universalHandJointGetter(0)
    fun getTorque(): HandJoint? = universalHandJointGetter(7)
    fun getSpeed(): HandJoint? = universalHandJointGetter(14)
    fun getTemperature(): HandJoint? = universalHandJointGetter(21)

    fun getNormalForce(): HandFinger? = universalHandJointGetter(35, 4, 5)
    fun getTangentialForce(): HandFinger? = universalHandJointGetter(36, 4, 5)
    fun getTangentialForceDirection(): HandFinger? = universalHandJointGetter(37, 4, 5)
    fun getApproachIncrement(): HandFinger? = universalHandJointGetter(38, 4, 5)

    fun getErrorCode() {
        //TODO("Not yet implemented")
    }

    private fun universalHandJointSetter(address: Int, value: HandJoint): Boolean {
        val part1 = "{${value.thumbPitch},${value.thumbYaw},${value.finger1},${value.finger2}}"
        val part2 = "{${value.finger3},${value.finger4},${value.thumbRoll}}"

        val r1 = modbus.setHoldRegs(modbusIndex, address, 4, part1)
        Thread.sleep(15) //防止总线拥堵
        val r2 = modbus.setHoldRegs(modbusIndex, address + 4, 3, part2)
        return r1.isSuccess() && r2.isSuccess()
    }

    private fun universalHandJointGetter(address: Int): HandJoint? {
        val r1 = modbus.getInRegs(modbusIndex, address, 4)
        Thread.sleep(15)
        val r2 = modbus.getInRegs(modbusIndex, address + 4, 3)

        return if (r1.isSuccess() && r2.isSuccess() && r1.refValues.size >= 4 && r2.refValues.size >= 3) {
            return HandJoint(
                r1.refValues[0].value as Int,
                r1.refValues[1].value as Int,
                r1.refValues[2].value as Int,
                r1.refValues[3].value as Int,
                r2.refValues[0].value as Int,
                r2.refValues[1].value as Int,
                r2.refValues[2].value as Int
            )
        } else null
    }

    private fun universalHandJointGetter(address: Int, int: Int): HandJoint? {
        val r1 = modbus.getHoldRegs(modbusIndex, address, 4)
        Thread.sleep(15)
        val r2 = modbus.getHoldRegs(modbusIndex, address + 4, 3)

        return if (r1.isSuccess() && r2.isSuccess() && r1.refValues.size >= 4 && r2.refValues.size >= 3) {
            return HandJoint(
                r1.refValues[0].value as Int,
                r1.refValues[1].value as Int,
                r1.refValues[2].value as Int,
                r1.refValues[3].value as Int,
                r2.refValues[0].value as Int,
                r2.refValues[1].value as Int,
                r2.refValues[2].value as Int
            )
        } else null
    }

    private fun universalHandJointGetter(startAddress: Int, step: Int, count: Int): HandFinger? {
        val valueList = mutableListOf<Int>()
        for (i in 0..<count) {
            val address = startAddress + i * step
            val r = modbus.getInRegs(modbusIndex, address, 1)
            if (r.isSuccess() && r.refValues.isNotEmpty()) {
                valueList.add(r.refValues[0].value as Int)
                Thread.sleep(5)
            }
        }
        return if (valueList.size == count) {
            HandFinger(
                valueList[0], valueList[1], valueList[2],
                valueList[3], valueList[4]
            )
        } else null
    }
}