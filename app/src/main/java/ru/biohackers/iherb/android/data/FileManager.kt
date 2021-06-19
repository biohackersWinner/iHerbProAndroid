package ru.biohackers.iherb.android.data

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.ResponseBody
import okio.buffer
import okio.sink
import okio.source
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.time.LocalDateTime
import javax.inject.Inject

class FileManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    fun savePrescriptionImageFile(imageUri: Uri): File {
//        val target = File(context.filesDir, file.name)
        val name = context.contentResolver.query(imageUri, null, null, null, null)
            ?.use { cursor ->
                val sizeIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                cursor.moveToFirst()
                cursor.getString(sizeIndex)
            } ?: LocalDateTime.now().toString() + ".png"

        val target = File(context.filesDir, name)
        val sink = target.sink().buffer()
        val inputStream = context.contentResolver.openInputStream(imageUri)!!
        inputStream.source().use { source ->
            var total: Long = 0
            var read: Long
            val bufferSize = DEFAULT_BUFFER_SIZE.toLong()
            while (source.read(sink.buffer, bufferSize).also { read = it } != -1L) {
                total += read
                sink.flush()
//                this.progressListener?.invoke(total, size + offset)
            }
        }
        return target
    }

    fun saveFile(body: ResponseBody, xlsName: String): File {
        val target = File(context.filesDir, xlsName)
        val sink = target.sink().buffer()
        body.byteStream().source().use { source ->
            var total: Long = 0
            var read: Long
            val bufferSize = DEFAULT_BUFFER_SIZE.toLong()
            while (source.read(sink.buffer, bufferSize).also { read = it } != -1L) {
                total += read
                sink.flush()
//                this.progressListener?.invoke(total, size + offset)
            }
        }
        return target
    }

}