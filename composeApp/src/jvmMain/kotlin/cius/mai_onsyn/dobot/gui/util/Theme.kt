package cius.mai_onsyn.dobot.gui.util

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

@Composable
fun AnimatedMaterialTheme(
    colorScheme: ColorScheme,
    content: @Composable () -> Unit
) {
    val kClass = ColorScheme::class
    val constructor = remember { kClass.primaryConstructor!! }
    val properties = remember { kClass.memberProperties }

    val args = constructor.parameters.associateWith { param ->
        val prop = properties.first { it.name == param.name }
        val value = prop.get(colorScheme)

        if (value is Color) {
            animateColorAsState(
                targetValue = value,
                animationSpec = tween(400),
                label = param.name ?: ""
            ).value
        } else {
            value
        }
    }

    val animatedScheme = remember(args) {
        constructor.callBy(args)
    }

    MaterialTheme(
        colorScheme = animatedScheme,
        typography = Typography(),
        content = content
    )
}