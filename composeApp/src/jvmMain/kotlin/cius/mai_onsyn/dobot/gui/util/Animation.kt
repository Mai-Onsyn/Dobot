package cius.mai_onsyn.dobot.gui.util

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

val tweenSpecFloat = tween<Float>(durationMillis = 300, easing = LinearOutSlowInEasing)
val tweenSpecDp = tween<Dp>(durationMillis = 300, easing = LinearOutSlowInEasing)
val tweenSpecColor = tween<Color>(durationMillis = 300, easing = LinearOutSlowInEasing)