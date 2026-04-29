package cius.mai_onsyn.dobot.gui.content.movement

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Tab
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import cius.mai_onsyn.dobot.gui.GLOBAL_PADDING
import cius.mai_onsyn.dobot.gui.util.tweenSpecFloat

@Composable
fun BoxScope.ArmControlPanel(
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    var selectedTabIndex by remember { mutableStateOf(0) }

    Column(
        modifier = modifier
    ) {
        PrimaryTabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth(),
            containerColor = TabRowDefaults.primaryContainerColor,
            contentColor = TabRowDefaults.primaryContentColor,
            indicator = {
                TabRowDefaults.PrimaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(selectedTabIndex, matchContentSize = true),
                    width = 114.dp,
                    height = 3.dp,
                    color = colorScheme.secondary
                )
            },
            divider = { HorizontalDivider() }
        ) {
            Tab(
                selected = selectedTabIndex == 0,
                onClick = { selectedTabIndex = 0 },
                text = { Text("关节运动") },
                icon = null
            )
            Tab(
                selected = selectedTabIndex == 1,
                onClick = { selectedTabIndex = 1 },
                text = { Text("坐标系运动") },
                icon = null
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = GLOBAL_PADDING),
            contentAlignment = Alignment.Center
        ) {
            AnimatedContent(
                targetState = selectedTabIndex,
                transitionSpec = {
                    fadeIn(animationSpec = tweenSpecFloat) + slideIn(
                        initialOffset = {
                            IntOffset(if (selectedTabIndex != 0) it.width / 4 else -it.width / 4, 0)
                        }
                    ) togetherWith
                            fadeOut(animationSpec = tweenSpecFloat)
                }
            ) {
                when (it) {
                    0 -> ArmJointControlPanel()
                    1 -> ArmCoordinateControlPanel()
                }
            }
        }
    }
}