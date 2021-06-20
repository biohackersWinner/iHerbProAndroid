package ru.biohackers.iherb.android.data

import ru.biohackers.iherb.android.data.Data.BADS
import ru.biohackers.iherb.android.data.Data.BAD_GROUPS
import ru.biohackers.iherb.android.data.Data.CATEGORIES
import ru.biohackers.iherb.android.data.Data.PRESCRIPTIONS
import ru.biohackers.iherb.android.model.Bad
import ru.biohackers.iherb.android.model.BadCategory
import ru.biohackers.iherb.android.model.BadGroup
import ru.biohackers.iherb.android.model.Prescription
import javax.inject.Inject


class Repository @Inject constructor(
    private val preferenceManager: PreferenceManager,
    private val fileManager: FileManager,
) {

    fun isAuthorized(): Boolean {
        return true
    }

    fun getPrescriptions(): List<Prescription> {
        return PRESCRIPTIONS.map { it.copy() } // hack
    }

    fun getBadById(id: Int): Bad? = BADS.find { it.id == id }

    fun getBadsByCategoryId(categoryId: Int): List<Bad> =
        BADS.filter { bad -> bad.categories.any { it.id == categoryId } }

    fun getBadsBySynonym(synonym: String): List<Bad> =
        BADS.filter { bad -> bad.synonyms.any { synonym.contains(it) } }

    fun getCategoryById(id: Int): BadCategory? = CATEGORIES.find { it.id == id }

    fun addPrescription(prescription: Prescription) {
        PRESCRIPTIONS.add(prescription.copy(id = (PRESCRIPTIONS.maxOfOrNull { it.id } ?: 0) + 1))
    }

    fun getBadGroupsBySynonyms(terms: List<String>): List<BadGroup> {
        return BAD_GROUPS.filter { badGroup: BadGroup ->
            terms.any { term ->
                badGroup.synonyms.any { syn ->
                    term.contains(
                        syn,
                        ignoreCase = true
                    )
                }
            }
        }
//        it.forEach { text ->
//            BAD_GROUPS.any { badGroup ->
//                badGroup.synonyms.any { syn ->
//                    text.contains(
//                        syn,
//                        ignoreCase = true
//                    )
//                }
//            }
//        }

    }

    fun checkIsHasBadGroup(text: String): Boolean =
        BAD_GROUPS.any { badGroup ->
            badGroup.synonyms.any { syn ->
                text.contains(
                    syn,
                    ignoreCase = true
                )
            }
        }


    fun getPrescription(prescriptionId: Int): Prescription? {
        return PRESCRIPTIONS.find { it.id == prescriptionId }
    }

}
