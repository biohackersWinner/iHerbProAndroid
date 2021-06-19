package ru.biohackers.iherb.android.model

data class Bad(
    val id: Int,
    val title: String,
    val imageUrl: String,
    val description: String,
    val synonyms: List<String>,
    val categories: List<BadCategory>,
    val characteristics: String, // todo можно сделать моделькой
)