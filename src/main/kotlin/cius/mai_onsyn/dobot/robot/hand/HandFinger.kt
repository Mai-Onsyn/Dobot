package cius.mai_onsyn.dobot.robot.hand

data class HandFinger(
    val thumb: Int,
    val index: Int,
    val middle: Int,
    val ring: Int,
    val little: Int
) {
    override fun toString(): String {
        return "{$thumb,$index,$middle,$ring,$little}"
    }

    companion object {
        val EMPTY = HandFinger(0, 0, 0, 0, 0)
    }
}