package cius.mai_onsyn.dobot.gui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cius.mai_onsyn.dobot.core.experience.Experiment
import cius.mai_onsyn.dobot.gui.content.Page

var currentPage by mutableStateOf(Page.EXPERIENCE)
var experimenting by mutableStateOf(false)
var expProgress by mutableStateOf(0.4f)

val currentExperiment = Experiment.METHYLENE_BLUE
val currentExperimentName = currentExperiment.getStepNameList()
var currentStep by mutableStateOf(-1)
var currentStartTime by mutableStateOf(0L)