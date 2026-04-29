package cius.mai_onsyn.dobot.gui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import cius.mai_onsyn.dobot.gui.left.GuidColumn
import cius.mai_onsyn.dobot.gui.top.TopBar
import cius.mai_onsyn.dobot.gui.util.AnimatedMaterialTheme
import cius.mai_onsyn.dobot.gui.util.Config.isDarkMode
import cius.mai_onsyn.dobot.gui.util.botDarkTheme
import cius.mai_onsyn.dobot.gui.util.botLightTheme
import cius.mai_onsyn.dobot.gui.util.interaction

@Composable
@Preview
fun App() {
    AnimatedMaterialTheme(
        colorScheme = if (isDarkMode) botDarkTheme else botLightTheme
    ) {
        val focusManager = LocalFocusManager.current
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .pointerInput(Unit) {
                    detectTapGestures {
                        focusManager.clearFocus()
                    }
                }
        ) {
            GuidColumn(
                modifier = Modifier.width(150.dp).zIndex(10f)
            )
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                TopBar(modifier = Modifier
                    .height(48.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(GLOBAL_PADDING)
                ) {
                    AnimatedContent(
                        targetState = currentPage,
                        transitionSpec = {
                            fadeIn(animationSpec = tween(220)) + scaleIn(initialScale = 0.92f) togetherWith
                                    fadeOut(animationSpec = tween(120))
                        }
                    ) { it.content() }
                }
            }
        }
    }
}