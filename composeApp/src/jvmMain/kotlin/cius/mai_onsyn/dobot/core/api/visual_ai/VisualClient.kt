package cius.mai_onsyn.dobot.core.api.visual_ai

import com.alibaba.fastjson2.JSON
import com.alibaba.fastjson2.JSONObject
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.util.Base64
import javax.imageio.ImageIO

/**
 * MB 滴定终点检测 API 客户端
 */
class VisualClient(
    private val url: String = "http://localhost",
    private val port: Int = 32256
) {

    private val client = HttpClient(CIO)

    /**
     * 调用预测接口
     * @param imageBytes 图片二进制数据
     * @param probThresh 置信度阈值 (可选)
     * @param areaThresh 面积比例阈值 (可选)
     */
    suspend fun predict(
        imageBytes: ByteArray,
        probThresh: Double = 0.5,
        areaThresh: Double = 0.001
    ): VisualResponse? {
        val url = "$url:$port/predict"

        // 1. 将图片转为 Base64 字符串
        val base64Image = Base64.getEncoder().encodeToString(imageBytes)

        // 2. 使用 Fastjson2 构建请求体
        val requestBody = JSONObject().apply {
            put("image", base64Image)
            put("prob_thresh", probThresh)
            put("area_thresh", areaThresh)
        }

        return try {
            // 3. 发送 POST 请求
            val response: HttpResponse = client.post(url) {
                contentType(ContentType.Application.Json)
                setBody(requestBody.toJSONString())
            }

            if (response.status == HttpStatusCode.OK) {
                // 4. 使用 Fastjson2 解析响应
                val responseText = response.bodyAsText()
                JSON.parseObject(responseText, VisualResponse::class.java)
            } else {
                println("请求失败: ${response.status}")
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun predictFromImage(image: BufferedImage): VisualResponse? {
        val bao = ByteArrayOutputStream()
        withContext(Dispatchers.IO) {
            ImageIO.write(image, "jpg", bao)
        }
        return predict(bao.toByteArray())
    }

    fun close() {
        client.close()
    }
}

/**
 * 响应数据模型
 */
data class VisualResponse(
    val status: String,       // "positive", "negative", "error"
    val confidence: Double,   // 置信度
    val areaRatio: Double,   // 面积占比
    val message: String?      // 错误信息或 success
)