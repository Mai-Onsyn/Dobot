package cius.mai_onsyn.dobot.gui.content.experience.log.realtime_data

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cius.mai_onsyn.dobot.gui.ROUND_CORNER_SHAPE
import dobot.composeapp.generated.resources.Res
import dobot.composeapp.generated.resources.icon_angle
import dobot.composeapp.generated.resources.icon_core_status
import dobot.composeapp.generated.resources.icon_device_status
import dobot.composeapp.generated.resources.icon_exp_res
import dobot.composeapp.generated.resources.icon_graph
import dobot.composeapp.generated.resources.icon_location
import dobot.composeapp.generated.resources.icon_log
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

/**
 * 监控数据状态实体
 */
data class MonitorData(
    val runningTime: String = "00:00:00",
    val trajectoryCount: Int = 0,
    val loopCount: Int = 0,
    val colorIndex: Float = 0f,
    val confidence: Float = 0f,
    val soilContent: Float = 0f,
    val robotPos: Triple<Float, Float, Float> = Triple(0f, 0f, 0f),
    val jointAngles: List<Float> = listOf(0f, 0f, 0f, 0f, 0f, 0f),
    val mixerSpeed: Int = 0,
    val elevatorStatus: String = "IDLE",
    val beakerStatus: String = "READY"
)

/**
 * 核心监控仪表盘组件
 * 可直接嵌入任何 UI 容器，自动填充宽度
 */
@Composable
fun MonitoringDashboard(data: MonitorData, modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 280.dp),
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        // 软件运行数据卡片
        item {
            MonitorCard("运行统计", Res.drawable.icon_graph) {
                MetricRow("进行时间", data.runningTime, "")
                MetricRow("轨迹数量", data.trajectoryCount.toString(), "条")
                MetricRow("循环轮数", data.loopCount.toString(), "轮")
            }
        }

        item {
            MonitorCard("核心指标", Res.drawable.icon_core_status) {
                MetricRow("色晕指标", "%.2f".format(data.colorIndex), "SI")
                MetricRow("置信度", "%.1f%%".format(data.confidence * 100), "")
                LinearProgressIndicator(
                    progress = { data.confidence },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    color = if (data.confidence > 0.9) Color(0xFF4CAF50) else Color(0xFFFFC107),
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            }
        }

        item {
            MonitorCard("实验结果", Res.drawable.icon_exp_res) {
                Spacer(modifier = Modifier.height(20.dp))
                MetricRow("第4轮滴定", "", "")
                Row(
                    verticalAlignment = Alignment.CenterVertically,
//                    modifier = Modifier.height(100.dp)
                ) {
                    Text(
                        text = "MB值",
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "%.2f".format(data.soilContent),
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "g/kg",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
//                MetricRow("MB值", "%.2f%%".format(data.soilContent), "")
//                Spacer(modifier = Modifier.height(8.dp))
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(32.dp)
//                        .clip(RoundedCornerShape(4.dp))
//                        .background(MaterialTheme.colorScheme.surfaceVariant)
//                ) {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth(data.soilContent / 100f)
//                            .fillMaxHeight()
//                            .background(MaterialTheme.colorScheme.primary)
//                    )
//                }
            }
        }

        // 硬件监控数据卡片
        item {
            MonitorCard("位置信息", Res.drawable.icon_location) {
                MetricRow("X轴坐标", "%.2f".format(data.robotPos.first), "mm")
                MetricRow("Y轴坐标", "%.2f".format(data.robotPos.second), "mm")
                MetricRow("Z轴坐标", "%.2f".format(data.robotPos.third), "mm")
            }
        }

        item {
            MonitorCard("关节角度", Res.drawable.icon_angle) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    JointGrid(data.jointAngles)
                }
            }
        }

        item {
            MonitorCard("设备状态", Res.drawable.icon_device_status) {
                MetricRow("搅拌机转速", data.mixerSpeed.toString(), "RPM")
                StatusRow("升降台", data.elevatorStatus)
                StatusRow("烧杯状态", data.beakerStatus)
            }
        }
    }
}

@Composable
private fun MonitorCard(title: String, icon: DrawableResource, content: @Composable ColumnScope.() -> Unit) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = ROUND_CORNER_SHAPE,
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painterResource(icon),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp), thickness = 0.5.dp)
            content()
        }
    }
}

@Composable
private fun MetricRow(label: String, value: String, unit: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Row(verticalAlignment = Alignment.Bottom) {
            Text(value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
            if (unit.isNotEmpty()) {
                Text(" $unit", style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(bottom = 2.dp, start = 2.dp))
            }
        }
    }
}

@Composable
private fun StatusRow(label: String, status: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        val color = when(status.uppercase()) {
            "RUNNING", "HEATING", "BUSY" -> Color(0xFF4CAF50)
            "STOPPED", "IDLE", "OFFLINE" -> Color(0xFFF44336)
            else -> Color(0xFF2196F3)
        }
        Box(
            modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(color.copy(alpha = 0.15f)).padding(horizontal = 8.dp, vertical = 2.dp)
        ) {
            Text(status, color = color, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.ExtraBold)
        }
    }
}

@Composable
private fun JointGrid(angles: List<Float>) {
    val chunked = angles.chunked(3)
    chunked.forEachIndexed { rowIndex, rowAngles ->
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            rowAngles.forEachIndexed { colIndex, angle ->
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                    Text("J${rowIndex * 3 + colIndex + 1}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                    Text("%.1f°".format(angle), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}