package cius.mai_onsyn.dobot.gui.content.experience.info

import cius.mai_onsyn.dobot.core.UIInterface
import cius.mai_onsyn.dobot.gui.currentExperiment
import cius.mai_onsyn.dobot.gui.currentExperimentName
import cius.mai_onsyn.dobot.gui.currentStartTime
import cius.mai_onsyn.dobot.gui.currentStep
import cius.mai_onsyn.dobot.gui.experimenting
import cius.mai_onsyn.dobot.log

fun onStartButtonClicked() {
    experimenting = !experimenting
    if (experimenting) onStart()
    else onStop()
}

fun onStart() {
    currentStartTime = System.currentTimeMillis()
    currentStep = 0
    Thread.ofVirtual().start {
        for (i in currentExperiment) {
            if (experimenting) {
                try {
                    currentExperimentName.indexOf(i.title).let {
                        if (it != -1) currentStep = it
                    }
                    UIInterface.app.executeLine(i.command)
                } catch (e: Exception) {
                    log.error(e.message)
                } finally {
                    Thread.sleep(2000)
                }
            }
        }
        currentStep = currentExperiment.size
    }
}

fun onStop() {
    currentStep = -1
    experimenting = false
}