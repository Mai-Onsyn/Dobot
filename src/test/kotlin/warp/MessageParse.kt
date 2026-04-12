package warp

import cius.mai_onsyn.dobot.api.socket.RobotResponse

fun main() {
    val response = RobotResponse.of("0,{a, c, efasa, 10},MovL(-500,100,200,150,0,90);")
    println(response)
}