package cius.mai_onsyn.dobot.core.api.socket

import cius.mai_onsyn.dobot.core.api.splitTopLevel

data class Message(
    val name: String,
    val params: List<ParamValue>
) {
    companion object {
        fun of(msg: String): Message {
            val name = msg.substring(0, msg.indexOf('('))
            val paramsString = msg.substring(msg.indexOf('(') + 1, msg.length - 1) // 提取()内容并删除()
//                .split(',')
//                .map { ParamValue.of(it.trim()) }
            val params = splitTopLevel(paramsString)
                .map { ParamValue.of(it.trim()) }
            return Message(name, params)
        }
    }

    override fun toString(): String {
        return "$name(${params.joinToString(",") { it.value.toString() }})"
    }
}
