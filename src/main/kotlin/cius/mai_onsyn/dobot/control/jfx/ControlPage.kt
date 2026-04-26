package cius.mai_onsyn.dobot.control.jfx

import cius.mai_onsyn.dobot.api.robot.DobotE6V4
import javafx.scene.layout.VBox
import mai_onsyn.AnimeFX.layout.AutoPane

fun controlPage(api: DobotE6V4): VBox {
    val pane = VBox()
    pane.spacing = 20.0

    val switchItem = SwitchItem("Test", true)
    switchItem.style = "-fx-background-color: #ff00ff"
    pane.children.addAll(switchItem)

    return pane
}