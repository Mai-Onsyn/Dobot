package cius.mai_onsyn.dobot.gui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import cius.mai_onsyn.dobot.gui.left.GuidColumn
import cius.mai_onsyn.dobot.gui.top.TopBar

@Composable
@Preview
fun App() {
    MaterialTheme(
        colorScheme = lightColorScheme()//darkColorScheme()
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
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
                Box(modifier = Modifier.fillMaxSize()) {
                    currentPage.content()
                }
            }
        }
    }
}