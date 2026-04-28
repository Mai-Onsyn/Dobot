package cius.mai_onsyn.dobot.gui.top

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun ThemeSwitch(
    modifier: Modifier = Modifier,
    isDark: Boolean,
    onToggle: (Boolean) -> Unit
) {
    val scheme = MaterialTheme.colorScheme

    Switch(
        modifier = modifier,
        checked = isDark,
        onCheckedChange = onToggle,

        colors = SwitchDefaults.colors(
            checkedThumbColor = scheme.primary,
            checkedTrackColor = scheme.primaryContainer,

            uncheckedThumbColor = scheme.outline,
            uncheckedTrackColor = scheme.surfaceVariant,

            checkedBorderColor = Color.Transparent,
            uncheckedBorderColor = Color.Transparent
        ),

        thumbContent = {
            Text(
                text = if (isDark) "暗" else "亮",
                color = scheme.onPrimary,
                style = MaterialTheme.typography.labelSmall,
            )
        }
    )
}