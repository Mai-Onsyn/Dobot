package cius.mai_onsyn.dobot.gui.content.trajectory.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cius.mai_onsyn.dobot.gui.GLOBAL_PADDING
import cius.mai_onsyn.dobot.gui.content.trajectory.points.TrajectoryPointsManager.selectedPointIds
import cius.mai_onsyn.dobot.gui.content.trajectory.points.TrajectoryPointsManager.workingTrajectory
import cius.mai_onsyn.dobot.gui.util.universal_module.layout.CardBase

@Composable
fun DetailModule(
    modifier: Modifier = Modifier
) {
    val point=if (selectedPointIds.size==1) {
        workingTrajectory.find { selectedPointIds.first() == it.id }
    }else null
    val title = if(point!=null){
        val index = workingTrajectory.indexOf(point)
        "点位详情(点${index+ 1})"
    }else{
        "点位详情"
    }
    CardBase(modifier = modifier) {
        Column (
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(vertical = GLOBAL_PADDING),
        ){

            Text(
                text = title,
                style = TextStyle(fontWeight = FontWeight.Bold),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = GLOBAL_PADDING).height(24.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            ){
                HorizontalDivider()
            }
            Spacer(modifier = Modifier.height(12.dp))
            if (selectedPointIds.size<=1) {
                Column(modifier = Modifier.fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = GLOBAL_PADDING)){
                    Text(
                        text = "机械臂关节",
                        style = TextStyle(fontWeight = FontWeight.Bold),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = GLOBAL_PADDING).height(24.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    DetailRow(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = GLOBAL_PADDING),
                        items = listOf(
                            "J1" to point?.point?.joint?.j1,
                            "J2" to point?.point?.joint?.j2,
                            "J3" to point?.point?.joint?.j3,
                            "J4" to point?.point?.joint?.j4,
                            "J5" to point?.point?.joint?.j5,
                            "J6" to point?.point?.joint?.j6
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "灵巧手关节",
                        style = TextStyle(fontWeight = FontWeight.Bold),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = GLOBAL_PADDING).height(24.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    DetailRow(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = GLOBAL_PADDING),
                        items = listOf(
                            "Tp" to point?.point?.hand?.thumbPitch?.toDouble(),   // 如果手部数据是 Int，转为 Double
                            "Ty" to point?.point?.hand?.thumbYaw?.toDouble(),
                            "F1" to point?.point?.hand?.finger1?.toDouble(),
                            "F2" to point?.point?.hand?.finger2?.toDouble(),
                            "F3" to point?.point?.hand?.finger3?.toDouble(),
                            "F4" to point?.point?.hand?.finger4?.toDouble(),
                            "Tr" to point?.point?.hand?.thumbRoll?.toDouble()
                        )
                    )
                }
            }else{
               Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                   Text(
                       text = "无法同时显示多个点坐标",
                       textAlign = TextAlign.Center,
                       color = MaterialTheme.colorScheme.onSurface,
                       modifier = Modifier.align(Alignment.Center)
                   )
               }
            }
        }
    }
}