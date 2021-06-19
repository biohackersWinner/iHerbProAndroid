package ru.biohackers.iherb.android.data.api

import com.google.gson.annotations.SerializedName
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*


interface ConvertioApiService {

    @POST("convert")
    suspend fun convert(@Body request: ConvertRequest): Response<ConvertResponse>

    @PUT("convert/{id}/results.png")
    suspend fun upload(
        @Path("id") id: String,
        @Body file: RequestBody,
    ): Response<UploadResponse>

    @GET("convert/{id}/status")
    suspend fun status(@Path("id") id: String): Response<StatusResponse>

    @Streaming
    @GET
    suspend fun downloadResult(@Url fileUrl: String): Response<ResponseBody>

}

data class ConvertRequest(
    val apikey: String,
    val input: String,
    val options: Options,
    val outputformat: String,
) {

    data class Options(
        val ocr_enabled: Boolean,
        val ocr_settings: OcrSettings
    ) {
        data class OcrSettings(
            val langs: List<String>
        )
    }
}

data class ConvertResponse(
    val code: Int,
    @SerializedName("data")
    val convertData: ConvertResponseData,
    val status: String
) {
    data class ConvertResponseData(
        val id: String
    )
}

data class UploadResponse(
    val code: Int,
    @SerializedName("data")
    val uploadData: UploadResponseData,
    val status: String
) {
    data class UploadResponseData(
        @SerializedName("file")
        val uploadedFile: String,
        val id: String,
        val size: Int
    )
}

data class StatusResponse(
    val code: Int,
    @SerializedName("data")
    val statusData: Data,
    val status: String
) {
    data class Data(
        val id: String,
        val minutes: String,
        val output: Output,
        val step: String,
        val step_percent: Int
    ) {
        data class Output(
            val size: String,
            val url: String
        )
    }
}