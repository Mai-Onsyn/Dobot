package cius.mai_onsyn.dobot.control

//import cius.mai_onsyn.dobot.api.DobotE6V4
//import javafx.application.Application
//import javafx.event.EventHandler
//import javafx.scene.Scene
//import javafx.scene.control.Button
//import javafx.scene.input.MouseDragEvent
//import javafx.scene.input.MouseEvent
//import javafx.scene.layout.Pane
//import javafx.scene.layout.Region
//import javafx.stage.Stage
//
//class ControlPanel: Application() {
//    override fun start(stage: Stage?) {
//        val root = Pane()
//        val scene = Scene(root, 800.0, 600.0)
//
//        val button = mkButton(text = "Button") { println("Clicked") }
//        root.children.addAll(button)
//
//        stage!!
//        stage.scene = scene
//        stage.show()
//    }
//
//    private fun mkButton(
//        width: Double = 80.0,
//        height: Double = 40.0,
//        text: String = "",
//        onClick: EventHandler<MouseEvent> = EventHandler{}
//    ): Button {
//        val b = Button(text)
//        b.prefWidth = width
//        b.prefHeight = height
//        b.onMouseClicked = onClick
//        return b
//    }
//
//    private fun initRobot(): DobotE6V4? {
//        val dot = DobotE6V4("192.168.5.1")
//        dot.connect()
//        val control = dot.control
//        val requestControl = control.requestControl()
//        if (!requestControl.isSuccess()) {
//            dot.disconnect()
//            return null
//        }
//        control.emergencyStop(false)
//        control.stop()
//        control.clearError()
//        control.enableRobot()
//
//        return dot
//    }
//
//    private fun addMoveListener(root: Region, camera: Camera) {
//        root.addEventFilter(MouseDragEvent.MOUSE_DRAGGED) {
//            camera.pos.x += it.x
//            camera.pos.z += it.y
//        }
//    }
//}