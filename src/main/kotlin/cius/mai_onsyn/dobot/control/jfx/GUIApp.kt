package cius.mai_onsyn.dobot.control.jfx

import cius.mai_onsyn.dobot.api.API
import cius.mai_onsyn.dobot.control.console.ConsoleApp
import javafx.scene.input.MouseEvent
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import mai_onsyn.AnimeFX.Module.AXButton
import mai_onsyn.AnimeFX.layout.AutoPane
import kotlin.concurrent.thread

fun mainStageContainer(api: API): AutoPane{
    val root = AutoPane()

//    val control = controlPage(api.robotApi)
//    root.children.addAll(control)
//    root.setPosition(control, false, 50.0, 150.0, 0.0, 0.0)
//    root.flipRelativeMode(control, AutoPane.Motion.LEFT)
    val app = ConsoleApp(api)
    thread(name = "console") {
        app.run()
    }


    val ga = cmdGroupA(app)
    val gb = cmdGroupB(app)
    root.children.addAll(VBox(ga, gb))

    return root
}


fun cmdGroupA(app: ConsoleApp): HBox {
    val box = HBox()

    val ms2g = buildButton("Move to", "record replay ms2g", app)
    val ms2gR = buildButton("Move to R", "record replay ms2g_r", app)
    val grabUp = buildButton("Grab Up", "record replay grab_up", app)
    val putEnter = buildButton("Put Enter", "record replay put_enter", app)
    val glassRepeat = buildButton("Repeat", "record replay glass_repeat", app)
    val glassRepeatR = buildButton("Repeat R", "record replay glass_repeat_r", app)
    val glassReset = buildButton("Reset", "record replay glass_reset", app)

    box.children.addAll(
        ms2g, ms2gR, grabUp, putEnter, glassRepeat, glassRepeatR, glassReset
    )

    return box
}

fun cmdGroupB(app: ConsoleApp): HBox {
    val box = HBox()
    val dropE = buildButton("Drop E", "record replay drope", app)
    val drop1 = buildButton("Drop 1", "record replay drop1", app)
    val drop3 = buildButton("Drop 3", "record replay drop3", app)
    val drop4 = buildButton("Drop 4", "record replay drop4", app)
    val drop6 = buildButton("Drop 6", "record replay drop6", app)

    box.children.addAll(
        dropE, drop1, drop3, drop4, drop6
    )

    return box
}

fun buildButton(label: String, cmd: String, app: ConsoleApp): AXButton {
    val button = AXButton(label)
    button.lockSize(100.0, 50.0)
    button.addEventFilter(MouseEvent.MOUSE_CLICKED) {
        Thread.ofVirtual().name("JFX-Runner").start { app.executeLine(cmd) }
    }
    return button
}