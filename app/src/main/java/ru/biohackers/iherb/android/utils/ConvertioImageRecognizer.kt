package ru.biohackers.iherb.android.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import ru.biohackers.iherb.android.data.FileManager
import ru.biohackers.iherb.android.data.api.ApiError.Companion.parseAsError
import ru.biohackers.iherb.android.data.api.ConvertRequest
import ru.biohackers.iherb.android.data.api.ConvertResponse
import ru.biohackers.iherb.android.data.api.ConvertioApiService
import ru.biohackers.iherb.android.data.api.StatusResponse
import java.io.File
import java.time.LocalDateTime
import javax.inject.Inject

class ConvertioImageRecognizer @Inject constructor(
    val convertioApiService: ConvertioApiService,
    val fileManager: FileManager,
) : ImageRecognizer {

    override suspend fun recognizeImage(file: File): String {
        val data: File = convertFileToTxt(file)
        return data.readText()
    }

    suspend fun convertFileToXlsx(file: File): File {
        return convertFile(file, "xlsx")
    }

    suspend fun convertFileToTxt(file: File): File {
        return convertFile(file, "txt")
    }

    suspend fun convertFile(file: File, format: String): File =
        withContext(Dispatchers.IO) {

            val convertResponse: Response<ConvertResponse> = convertioApiService.convert(
                ConvertRequest(
                    apikey = "fc45eea3301c6f1d613d25321bbd688a",
                    input = "upload",
                    options = ConvertRequest.Options(
                        ocr_enabled = true,
                        ocr_settings = ConvertRequest.Options.OcrSettings(
                            langs = listOf("rus")
                        )
                    ),
                    outputformat = format
                )
            )

            if (!convertResponse.isSuccessful) {
                throw Exception(
                    convertResponse.errorBody()?.parseAsError()?.error ?: "Неизвестная ошибка"
                )
            }

            val id = convertResponse.body()?.convertData?.id
                ?: throw IllegalStateException("Can't parse id")

            val requestFile: RequestBody = file.asRequestBody()
            val uploadResponse = convertioApiService.upload(id, requestFile)

            if (!uploadResponse.isSuccessful) {
                throw Exception(
                    Error(
                        uploadResponse.errorBody()?.parseAsError()?.error ?: "Неизвестная ошибка"
                    )
                )
            }

            var tries = 0
            var statusResponseBody: StatusResponse? = null
            do {
                delay(3000)
                tries++
                val statusResponse: Response<StatusResponse> = convertioApiService.status(id)
                if (!uploadResponse.isSuccessful) {
                    throw Exception(
                        uploadResponse.errorBody()?.parseAsError()?.error ?: "Неизвестная ошибка"
                    )
                }
                val processingCompleteOnServer = if (statusResponse.body() != null) {
                    statusResponseBody = statusResponse.body()!!
                    statusResponseBody.statusData.step == "finish"
                } else {
                    false
                }
            } while (!processingCompleteOnServer && tries < 5)

            val downloadResponse =
                convertioApiService.downloadResult(statusResponseBody?.statusData?.output?.url!!)
                    .body()!!
            val xlsFile =
                fileManager.saveFile(downloadResponse, LocalDateTime.now().toString() + ".format")
            xlsFile
        }
}