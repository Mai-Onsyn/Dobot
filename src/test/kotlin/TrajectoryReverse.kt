import com.alibaba.fastjson2.JSONArray
import com.alibaba.fastjson2.JSONWriter
import java.io.File
import java.nio.file.Files

fun main() {
    val jsonArray = JSONArray.parseArray(Files.readString(File("ms2g.json").toPath()))
//    println(jsonArray.toString(JSONWriter.Feature.PrettyFormat))
    jsonArray.reverse()
    println(jsonArray.toString(JSONWriter.Feature.PrettyFormat))
}