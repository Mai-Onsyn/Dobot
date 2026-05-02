package cius.mai_onsyn.dobot.gui

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application

fun main() {
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Dobot",
            state = WindowState(width = 1440.dp, height = 900.dp)
        ) {
            App()
        }
    }
}