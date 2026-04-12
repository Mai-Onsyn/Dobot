package cius.mai_onsyn.dobot.api.socket

enum class ParamType {
    INT, STRING, DOUBLE, BOOL
}

abstract class ParamValue(val type: ParamType) {
    abstract val value: Any
    companion object {
        fun of(value: String): ParamValue {
            return when {
                value.toIntOrNull() != null -> IntValue(value.toInt())
                value.toDoubleOrNull() != null -> DoubleValue(value.toDouble())
                value.equals("true", true) || value.equals("false", true) -> BoolValue(value.toBoolean())
                else -> StringValue(value)
            }
        }
    }

    override fun toString(): String {
        return value.toString()
    }
}

class IntValue(val intValue: Int) : ParamValue(ParamType.INT) {
    override val value: Any get() = intValue
}
class DoubleValue(val doubleValue: Double) : ParamValue(ParamType.DOUBLE) {
    override val value: Any get() = doubleValue
}
class BoolValue(val boolValue: Boolean) : ParamValue(ParamType.BOOL) {
    override val value: Any get() = boolValue
}
class StringValue(val stringValue: String) : ParamValue(ParamType.STRING) {
    override val value: Any get() = stringValue
}