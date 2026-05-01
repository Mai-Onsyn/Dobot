package cius.mai_onsyn.dobot.gui.content.movement

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cius.mai_onsyn.dobot.core.UIInterface.api
import cius.mai_onsyn.dobot.gui.GLOBAL_PADDING
import cius.mai_onsyn.dobot.gui.util.Config.armStep
import cius.mai_onsyn.dobot.gui.util.universal_module.CardBase
import cius.mai_onsyn.dobot.gui.util.universal_module.LabeledItem
import cius.mai_onsyn.dobot.gui.util.universal_module.PointSnapSlider
import cius.mai_onsyn.dobot.gui.util.universal_module.SliderWithInput
import cius.mai_onsyn.dobot.robot.arm.Joint

@Composable
fun ArmJointControlPanel(
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    CardBase(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            val jointValues = rememberSaveable {
                mutableStateListOf(0f, 0f, 0f, 0f, 0f, 0f)
            }
            val joint by remember {
                derivedStateOf {
                    Joint(
                        jointValues[0].toDouble(),
                        jointValues[1].toDouble(),
                        jointValues[2].toDouble(),
                        jointValues[3].toDouble(),
                        jointValues[4].toDouble(),
                        jointValues[5].toDouble()
                    )
                }
            }
            val jointList = listOf("J1", "J2", "J3", "J4", "J5", "J6")
            jointList.forEachIndexed{ index, value ->
                LabeledItem(
                    label = value,
                    modifier = Modifier
                        .height(40.dp)
                        .padding(start = GLOBAL_PADDING),
                    labelWidth = 20.dp,
                    fontSize = 16.sp,
                    textColor = colorScheme.onBackground
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxSize()
                    ) {

                        Button(
                            contentPadding = PaddingValues.Zero,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = colorScheme.onBackground
                            ),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 0.dp,
                                pressedElevation = 0.dp,
                                focusedElevation = 0.dp,
                                hoveredElevation = 0.dp,
                                disabledElevation = 0.dp
                            ),
                            onClick = { jointValues[index] -= armStep },
                            modifier = Modifier.size(35.dp)
                        ) {
                            Text("-")
                        }
                        Button(
                            contentPadding = PaddingValues.Zero,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = colorScheme.onBackground
                            ),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 0.dp,
                                pressedElevation = 0.dp,
                                focusedElevation = 0.dp,
                                hoveredElevation = 0.dp,
                                disabledElevation = 0.dp
                            ),
                            onClick = { jointValues[index] += armStep },
                            modifier = Modifier.size(35.dp)
                        ) {
                            Text("+")
                        }

                        SliderWithInput(
                            value = jointValues[index],
                            onValueChange = { jointValues[index] = it },
                            range = -180f..180f,
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }
                }
            }

            Spacer(Modifier.height(20.dp))
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(Modifier.height(20.dp))

            LabeledItem(
                label = "步进",
                modifier = Modifier
                    .height(60.dp)
                    .padding(start = GLOBAL_PADDING),
                labelWidth = 40.dp,
                textColor = colorScheme.onBackground
            ) {
//                SliderWithInput(
//                    steps = 198,
//                    value = armStep,
//                    onValueChange = { armStep = it },
//                    range = 0.05f..10f,
//                    modifier = Modifier.fillMaxSize()
//                )

                PointSnapSlider(
                    points = listOf(0.01f, 0.05f, 0.1f, 0.5f, 1f, 5f, 10f, 50f),
                    value = armStep,
                    modifier = Modifier.fillMaxSize(),
                    onChanged = { armStep = it }
                )
            }

            Spacer(Modifier.height(20.dp))

        }
    }
}