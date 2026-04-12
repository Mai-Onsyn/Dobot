package cius.mai_onsyn.dobot.api

fun StringBuilder.appendUserTool(user: Int, tool: Int): StringBuilder {
    if (user != -1) { this.append(",user=").append(user.coerceIn(0..50)) }
    if (tool != -1) { this.append(",tool=").append(tool.coerceIn(0..50)) }
    return this
}

fun splitTopLevel(input: String): List<String> {
    val result = mutableListOf<String>()
    val current = StringBuilder()
    var parenDepth = 0  // ()
    var braceDepth = 0  // {}
    for (c in input) {
        when (c) {
            '(' -> {
                parenDepth++
                current.append(c)
            }
            ')' -> {
                parenDepth--
                current.append(c)
            }
            '{' -> {
                braceDepth++
                current.append(c)
            }
            '}' -> {
                braceDepth--
                current.append(c)
            }
            ',' -> {
                if (parenDepth == 0 && braceDepth == 0) {
                    result.add(current.toString())
                    current.clear()
                } else {
                    current.append(c)
                }
            }
            else -> current.append(c)
        }
    }
    if (current.isNotEmpty()) {
        result.add(current.toString())
    }
    return result
}