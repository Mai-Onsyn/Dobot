package cius.mai_onsyn.dobot.control.console

interface Command {
    val name: String
    val description: String
    fun execute(args: List<String>)
}