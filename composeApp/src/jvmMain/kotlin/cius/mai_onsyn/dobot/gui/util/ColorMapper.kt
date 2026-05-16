package cius.mai_onsyn.dobot.gui.util

import kotlin.math.exp
import kotlin.math.sin
import kotlin.math.cos
import kotlin.math.absoluteValue

class ColorMapper(
    private val seed: Long = 42L,
    private val gain: Float = 5.0f   // 增益因子，越大对差异越敏感
) {
    private val projectionMatrix: Array<FloatArray> = run {
        val rand = java.util.Random(seed)
        Array(3) { FloatArray(13) { rand.nextGaussian().toFloat() } }
    }

    fun pointToRgb(point: FloatArray): Triple<Float, Float, Float> {
        require(point.size == 13)

        // 投影到 3 维
        val y = FloatArray(3) { i ->
            var sum = 0f
            for (j in 0 until 13) {
                sum += projectionMatrix[i][j] * point[j]
            }
            sum
        }

        // 应用增益，再通过 Sigmoid 压缩到 (0,1)
        val rgb = y.map { 1f / (1f + exp(-(it * gain).toDouble())).toFloat() }

        return Triple(rgb[0], rgb[1], rgb[2])
    }
}

class PointColorGenerator {

    /**
     * 将13维坐标点转换为RGB颜色值（Int类型，格式为 0xRRGGBB）
     * @param point 长度为13的数组，元素值已归一化到 0.0 ~ 1.0 之间
     */
    fun generateColor(point: FloatArray): Int {
        require(point.size == 13) { "点坐标必须精确包含13个分量" }

        // 1. 计算 HUE (色相): 范围 0.0 ~ 360.0
        // 混合低频（全局接近度）和高频（局部敏感度）
        var hueAccumulator = 0.0
        point.forEachIndexed { index, v ->
            // 低频项：保证大体趋势
            hueAccumulator += v * 20.0
            // 高频项：利用正弦函数和质数频率，使得微小变化产生剧烈扰动
            hueAccumulator += sin(v * (index + 3) * 7.5) * 45.0
        }
        val hue = (hueAccumulator.absoluteValue % 360.0)

        // 2. 计算 SATURATION (饱和度): 范围 0.0 ~ 1.0
        // 保证颜色鲜艳，避免过多灰色导致无法分辨
        var satAccumulator = 0.0
        point.forEachIndexed { index, v ->
            satAccumulator += cos(v * (index + 1) * 11.3)
        }
        val saturation = 0.6 + (sin(satAccumulator).absoluteValue * 0.4) // 锁定在 0.6 ~ 1.0

        // 3. 计算 LIGHTNESS (亮度): 范围 0.0 ~ 1.0
        // 避免过暗(黑)或过亮(白)，锁定在中间高辨识度区间
        var lightAccumulator = 0.0
        point.forEachIndexed { index, v ->
            lightAccumulator += sin(v * (index + 2) * 9.7)
        }
        val lightness = 0.4 + (cos(lightAccumulator).absoluteValue * 0.3) // 锁定在 0.4 ~ 0.7

        return hslToRgb(hue, saturation, lightness)
    }

    /**
     * HSL 转 RGB 工具函数
     */
    private fun hslToRgb(h: Double, s: Double, l: Double): Int {
        val c = (1.0 - (2.0 * l - 1.0).absoluteValue) * s
        val x = c * (1.0 - ((h / 60.0) % 2.0 - 1.0).absoluteValue)
        val m = l - c / 2.0

        val (rPrime, gPrime, bPrime) = when ((h / 60.0).toInt()) {
            0 -> Triple(c, x, 0.0)
            1 -> Triple(x, c, 0.0)
            2 -> Triple(0.0, c, x)
            3 -> Triple(0.0, x, c)
            4 -> Triple(x, 0.0, c)
            else -> Triple(c, 0.0, x)
        }

        val r = ((rPrime + m) * 255).toInt().coerceIn(0, 255)
        val g = ((gPrime + m) * 255).toInt().coerceIn(0, 255)
        val b = ((bPrime + m) * 255).toInt().coerceIn(0, 255)

        return (0xff shl 24) or (r shl 16) or (g shl 8) or b
    }
}