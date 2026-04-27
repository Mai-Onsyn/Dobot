package cius.mai_onsyn.dobot.gui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cius.mai_onsyn.dobot.gui.content.Page

var currentPage by mutableStateOf(Page.ADJUST)