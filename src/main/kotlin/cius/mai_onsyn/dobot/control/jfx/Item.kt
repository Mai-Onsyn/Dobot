package cius.mai_onsyn.dobot.control.jfx

import javafx.scene.control.Label
import mai_onsyn.AnimeFX.Module.AXSwitch
import mai_onsyn.AnimeFX.layout.AutoPane


private const val HEIGHT = 40f


class SwitchItem(
    label: String,
    open: Boolean = false,
    onSwitch: (Boolean) -> Unit = {}
): AutoPane() {
    private val switch = AXSwitch(open)
    init {
        val label = Label(label)
        switch.stateProperty().addListener { _, _, v -> onSwitch(v) }
        super.children.addAll(label, switch)
        super.setPosition(label, true, 0.0, 0.4, 0.0, 0.0)
        super.setPosition(switch, false, 40.0, 0.0, 0.0, 0.0)
        super.flipRelativeMode(switch, Motion.LEFT)
//        super.lock
    }

    fun toggle(state: Boolean) {
        switch.stateProperty().set(state)
    }
}