package cius.mai_onsyn.dobot.core.control

import kotlin.math.sqrt

data class Vector(
    var x: Double,
    var y: Double,
    var z: Double
) {
    override fun toString(): String = "(x=$x, y=$y, z=$z)"

    operator fun plus(other: Vector): Vector =
        Vector(x + other.x, y + other.y, z + other.z)

    operator fun minus(other: Vector): Vector =
        Vector(x - other.x, y - other.y, z - other.z)

    operator fun Vector.unaryMinus(): Vector =
        Vector(-x, -y, -z)

    operator fun Vector.unaryPlus(): Vector = this

    operator fun times(other: Double): Vector =
        Vector(x * other, y * other, z * other)

    operator fun times(other: Int): Vector =
        Vector(x * other, y * other, z * other)

    operator fun times(other: Vector): Vector =
        Vector(x * other.x, y * other.y, z * other.z)

    operator fun div(other: Double): Vector =
        Vector(x / other, y / other, z / other)

    operator fun div(other: Int): Vector =
        Vector(x / other, y / other, z / other)

    operator fun plusAssign(other: Vector) {
        x += other.x
        y += other.y
        z += other.z
    }

    operator fun minusAssign(other: Vector) {
        x -= other.x
        y -= other.y
        z -= other.z
    }

    fun length(): Double = sqrt(x * x + y * y + z * z)
    
    fun normalize(): Vector {
        val len = length()
        return Vector(x / len, y / len, z / len)
    }

    fun cross(other: Vector): Vector {
        return Vector(
            y * other.z - z * other.y,
            z * other.x - x * other.z,
            x * other.y - y * other.x
        )
    }

    fun copy(): Vector = Vector(x, y, z)
    
    companion object {
        fun cross(a: Vector, b: Vector): Vector = a.cross(b)
        
        val ZERO = Vector(0.0, 0.0, 0.0)
        val X = Vector(1.0, 0.0, 0.0)
        val Y = Vector(0.0, 1.0, 0.0)
        val Z = Vector(0.0, 0.0, 1.0)
    }
}