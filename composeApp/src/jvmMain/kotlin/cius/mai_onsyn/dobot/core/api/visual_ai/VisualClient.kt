package cius.mai_onsyn.dobot.core.api.visual_ai

import cius.mai_onsyn.dobot.log
import com.alibaba.fastjson2.JSON
import com.alibaba.fastjson2.JSONObject
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.util.Base64
import javax.imageio.ImageIO

class VisualClient(
    private val url: String = "http://localhost",
    private val port: Int = 32256
): AutoCloseable {
    private val client = HttpClient(CIO)

    private suspend fun predict(
        imageBytes: ByteArray,
        probThresh: Double = 0.5,
        areaThresh: Double = 0.001
    ): VisualResponse? {
        val url = "$url:$port/predict"

        val base64Image = Base64.getEncoder().encodeToString(imageBytes)

        val requestBody = JSONObject().apply {
            put("image", base64Image)
            put("prob_thresh", probThresh)
            put("area_thresh", areaThresh)
        }

        return try {
            val response: HttpResponse = client.post(url) {
                contentType(ContentType.Application.Json)
                setBody(requestBody.toJSONString())
            }

            if (response.status == HttpStatusCode.OK) {
                val responseText = response.bodyAsText()
                JSON.parseObject(responseText, VisualResponse::class.java)
            } else {
                log.error("请求失败: ${response.status}")
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun predictFromImage(image: BufferedImage): VisualResponse? = runBlocking {
        val bao = ByteArrayOutputStream()
        withContext(Dispatchers.IO) {
            ImageIO.write(image, "jpg", bao)
        }
        predict(bao.toByteArray())
    }

    override fun close() {
        client.close()
    }
}

data class VisualResponse(
    val status: String,       // "positive", "negative", "error"
    val confidence: Double,   // 置信度
    val areaRatio: Double,    // 面积占比
    val message: String?      // 错误信息或 success
)