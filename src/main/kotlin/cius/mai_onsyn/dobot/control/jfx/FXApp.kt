package cius.mai_onsyn.dobot.control.jfx;

import cius.mai_onsyn.dobot.api.API
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.input.MouseEvent
import javafx.stage.Stage

class FXApp(): Application() {
    override fun start(stage: Stage) {
        val root = mainStageContainer(api!!)

        root.addEventFilter(MouseEvent.MOUSE_PRESSED) { root.requestFocus() }

        stage.scene = Scene(root, 800.0, 600.0)
        stage.show()
    }

    companion object {
        private var api: API? = null

        fun launch(api: API) {
            this.api = api
            launch(FXApp::class.java)
        }
    }
}