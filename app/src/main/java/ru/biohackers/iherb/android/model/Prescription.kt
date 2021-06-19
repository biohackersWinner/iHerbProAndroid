package ru.biohackers.iherb.android.model

import org.threeten.bp.LocalDateTime

data class Prescription(
    val id: Int,
    val name: String,
    val date: LocalDateTime,
    val fileLocation: String,
    val badGroups: List<BadGroup>,
)
