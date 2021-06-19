package ru.biohackers.iherb.android.utils

import android.content.Context
import android.graphics.*
import android.util.Base64
import android.util.Log
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okio.buffer
import okio.sink
import java.io.ByteArrayOutputStream
import java.io.File
import javax.inject.Inject

class YCloudImageRecognizer @Inject constructor(
    @ApplicationContext val context: Context,
    val okHttp: OkHttpClient,
    val gson: Gson,
) : ImageRecognizer {

    override suspend fun recognizeImage(file: File): String = withContext(Dispatchers.IO) {
        val bmp: Bitmap = BitmapFactory.decodeFile(file.path)
        val smallBmp = bmp.resize(1300, 1300)
        val data = smallBmp.encodeToBase64()!!

        val target = File(context.cacheDir, "body.json")

        target.sink().buffer().use { sink ->
            sink.writeUtf8(TEMPLATE_START)
                .writeUtf8(data)
                .writeUtf8(TEMPLATE_END)
                .writeUtf8("\n")
        }

        val mediaType: MediaType =
            "image/jpg".toMediaTypeOrNull() ?: throw IllegalArgumentException("media")
        val requestBody = target.asRequestBody(mediaType)

        val request: Request = Request.Builder()
            .url("https://vision.api.cloud.yandex.net/vision/v1/batchAnalyze")
            .post(requestBody)
            .addHeader(
                "Authorization",
                "Bearer t1.9euelZrGzMuZksrGlY-dy52VncfMnu3rnpWaiYqNmpLPzo_Ji4ySkZCZnZbl8_dlDEZ5-e86UlcT_t3z9yU7Q3n57zpSVxP-.lhOfP1uuyg_QO_aSqHMEIzFykvylWmtcBbHiqhERNpuL6N-eN7RKN5ipLGkJT2pPZf1kF03yzpIXDHL4VyIIAQ"
            )
            .addHeader("Content-Type", "application/json")
            .build()

        val execute = okHttp.newCall(request).execute()
        val result = execute.body?.string()!!
        val entity = gson.fromJson(result, YCloudResponse::class.java)

        val page = entity
            .results[0]
            .results[0]
            .textDetection.pages[0]


        Log.d(
            "!!!",
            "YC parsed page, blocks=${page.blocks.size}, w=${page.width}, h=${page.height}"
        )
        File(
            context.filesDir, "response.json"
        ).writeText(result)

        result
    }

    fun Bitmap.encodeToBase64(): String? {
        val baos = ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.JPEG, 90, baos)
        val b: ByteArray = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    fun decodeBase64(input: String?): Bitmap? {
        val decodedByte: ByteArray = Base64.decode(input, 0)
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.size)
    }

    private fun Bitmap.resize(maxWidth: Int, maxHeight: Int): Bitmap {
        return if (maxHeight > 0 && maxWidth > 0) {
            val width = this.width
            val height = this.height
            val ratioBitmap = width.toFloat() / height.toFloat()
            val ratioMax = maxWidth.toFloat() / maxHeight.toFloat()
            var finalWidth = maxWidth
            var finalHeight = maxHeight
            if (ratioMax > ratioBitmap) {
                finalWidth = (maxHeight.toFloat() * ratioBitmap).toInt()
            } else {
                finalHeight = (maxWidth.toFloat() / ratioBitmap).toInt()
            }
            Bitmap.createScaledBitmap(this, finalWidth, finalHeight, true)
        } else this
    }


    fun Bitmap.colorToGrayscale(): Bitmap {
        val grayScale = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.RGB_565)
        val cm = ColorMatrix()
        cm.setSaturation(0f)
        val p = Paint()
        p.colorFilter = ColorMatrixColorFilter(cm)
        Canvas(grayScale).drawBitmap(this, 0f, 0f, p)
        return grayScale
    }

    fun Bitmap.grayscaleToBin(threshold: Int): Bitmap {
        val bin = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.RGB_565)
        val cm = ColorMatrix(
            floatArrayOf(
                85f, 85f, 85f, 0f, -255f * threshold,
                85f, 85f, 85f, 0f, -255f * threshold,
                85f, 85f, 85f, 0f, -255f * threshold,
                0f, 0f, 0f, 1f, 0f
            )
        )
        val p = Paint()
        p.colorFilter = ColorMatrixColorFilter(cm)
        Canvas(bin).drawBitmap(this, 0f, 0f, p)
        return bin
    }

    fun Bitmap.otsuThreshold(): Bitmap {
        // Get Histogram
        val histogram = IntArray(256)
        for (i in histogram.indices) histogram[i] = 0
        for (i in 0 until this.width) {
            for (j in 0 until this.height) {
                histogram[this.getPixel(i, j) and 0xFF0000 shr 16]++
            }
        }

        // Get binary threshold using Otsu's method
        val total = this.height * this.width
        var sum = 0f
        for (i in 0..255) sum += (i * histogram[i]).toFloat()
        var sumB = 0f
        var wB = 0
        var wF = 0
        var varMax = 0f
        var threshold = 0
        for (i in 0..255) {
            wB += histogram[i]
            if (wB == 0) continue
            wF = total - wB
            if (wF == 0) break
            sumB += (i * histogram[i]).toFloat()
            val mB = sumB / wB
            val mF = (sum - sumB) / wF
            val varBetween = wB.toFloat() * wF.toFloat() * (mB - mF) * (mB - mF)
            if (varBetween > varMax) {
                varMax = varBetween
                threshold = i
            }
        }
        return grayscaleToBin(threshold)
    }

    companion object {
        const val TEMPLATE_START: String = """{
    "folderId": "b1gol9k45u6e7okiv2il",
    "analyze_specs": [{
        "content": """"
        const val TEMPLATE_END: String = """",
        "features": [{
            "type": "TEXT_DETECTION",
            "text_detection_config": {
                "language_codes": ["ru"]
            }
        }]
    }]
}           
"""
    }

}


data class YCloudResponse(
    val results: List<Result>
)

data class Result(
    val results: List<ResultX>
)

data class ResultX(
    val textDetection: TextDetection
)

data class TextDetection(
    val pages: List<Page>
)

data class Page(
    val blocks: List<Block>,
    val height: String,
    val width: String
)

data class Block(
    val boundingBox: BoundingBox,
    val lines: List<Line>
)

data class BoundingBox(
    val vertices: List<Vertice>
)

data class Line(
    val boundingBox: BoundingBox,
    val confidence: Double,
    val words: List<Word>
)

data class Vertice(
    val x: Int,
    val y: Int,
)

data class Word(
    val boundingBox: BoundingBox,
    val confidence: Double,
    val entityIndex: String,
    val languages: List<Language>,
    val text: String
)

data class Language(
    val confidence: Double,
    val languageCode: String
)
