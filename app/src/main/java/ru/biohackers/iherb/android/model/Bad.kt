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

data class BadGroup(
    val id: Int,
    val title: String,
    val synonyms: List<String>,
    val bads: List<Bad>,
)

data class BadCategory(
    val id: Int,
    val title: String,
)