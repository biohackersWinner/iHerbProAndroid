package ru.biohackers.iherb.android.data.api

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import okio.source

class ContentUriRequestBody(
    private val contentResolver: ContentResolver,
    private val contentUri: Uri,
    var mediaType: MediaType? = null,
    var offset: Long = 0,
    private val progressListener: ((downloaded: Long, total: Long) -> Unit)? = null
) : RequestBody() {

    private val size: Long =
        getFileSizeByUri(contentResolver, contentUri)
            ?: throw IllegalStateException("Can't retrieve media file size")

    override fun contentType(): MediaType? =
        mediaType ?: contentResolver.getType(contentUri)?.toMediaTypeOrNull()

    override fun writeTo(sink: BufferedSink) {
        val inputStream = contentResolver.openInputStream(contentUri)
            ?: throw IllegalStateException("Couldn't open content URI for reading: $contentUri")
        inputStream.skip(offset)

        inputStream.source().use { source ->
            var total: Long = 0
            var read: Long
            val bufferSize = DEFAULT_BUFFER_SIZE.toLong()
            while (source.read(sink.buffer, bufferSize).also { read = it } != -1L) {
                total += read
                sink.flush()
                this.progressListener?.invoke(total, size + offset)
            }
        }
    }

    override fun toString(): String =
        "contentUri=$contentUri, offset=$offset, size=$size"

    override fun contentLength(): Long = size - offset
}

fun getFileSizeByUri(
    contentResolver: ContentResolver,
    uri: Uri
): Long? {
    return calculateFileSize(contentResolver, uri)
        ?: contentResolver.query(uri, null, null, null, null)
            ?.use { cursor ->
                val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                cursor.moveToFirst()
                cursor.getLong(sizeIndex)
            }
}

private fun calculateFileSize(
    contentResolver: ContentResolver,
    uri: Uri
): Long? {
    val fileInputStream = contentResolver.openInputStream(uri)
    val size = fileInputStream?.available()?.toLong()
    fileInputStream?.close()
    return size
}