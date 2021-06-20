package ru.biohackers.iherb.android.utils

import java.io.File

interface ImageRecognizer {
    suspend fun recognizeImage(file: File): Pair<List<String>, File>
}