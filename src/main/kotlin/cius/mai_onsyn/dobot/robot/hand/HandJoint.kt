package cius.mai_onsyn.dobot.robot.hand

data class HandJoint(
    val thumbPitch: Int,
    val thumbYaw: Int,
    val finger1: Int,
    val finger2: Int,
    val finger3: Int,
    val finger4: Int,
    val thumbRoll: Int
) {
    override fun toString(): String {
        return "{$thumbPitch,$thumbYaw,$finger1,$finger2,$finger3,$finger4,$thumbRoll}"
    }

    fun toIntArray(): IntArray {
        return intArrayOf(thumbPitch, thumbYaw, finger1, finger2, finger3, finger4, thumbRoll)
    }

    companion object {
        val OPEN = HandJoint(255, 255, 255, 255, 255, 255, 255)
        val FRIENDLY_POSE = HandJoint(60, 128, 40, 255, 40, 40, 255)

        fun byIntArray(joints: IntArray): HandJoint {
            return HandJoint(joints[0], joints[1], joints[2], joints[3], joints[4], joints[5], joints[6])
        }
    }
}
