package ru.biohackers.iherb.android.data

import ru.biohackers.iherb.android.data.Data.BADS
import ru.biohackers.iherb.android.data.Data.CATEGORIES
import ru.biohackers.iherb.android.data.Data.PRESCRIPTIONS
import ru.biohackers.iherb.android.data.api.ConvertioApiService
import ru.biohackers.iherb.android.model.Bad
import ru.biohackers.iherb.android.model.BadCategory
import ru.biohackers.iherb.android.model.BadGroup
import ru.biohackers.iherb.android.model.Prescription
import javax.inject.Inject


class Repository @Inject constructor(
    private val preferenceManager: PreferenceManager,
    private val convertioApiService: ConvertioApiService,
    private val fileManager: FileManager,
) {

    fun isAuthorized(): Boolean {
        return true
    }

    fun getPrescriptions(): List<Prescription> = PRESCRIPTIONS

    fun getBadById(id: Int): Bad? = BADS.find { it.id == id }

    fun getBadsByCategoryId(categoryId: Int): List<Bad> =
        BADS.filter { bad -> bad.categories.any { it.id == categoryId } }

    fun getBadsBySynonym(synonym: String): List<Bad> =
        BADS.filter { bad -> bad.synonyms.any { synonym.contains(it) } }

    fun getCategoryById(id: Int): BadCategory? = CATEGORIES.find { it.id == id }

    fun addPrescription(prescription: Prescription) {
        PRESCRIPTIONS.add(prescription)
    }

    fun getBadGroupsBySynonyms(it: List<String>): List<BadGroup> {
            return listOf()
    }

}
