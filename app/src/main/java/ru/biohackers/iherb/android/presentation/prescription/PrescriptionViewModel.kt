package ru.biohackers.iherb.android.presentation.prescription

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime
import ru.biohackers.iherb.android.data.FileManager
import ru.biohackers.iherb.android.data.Repository
import ru.biohackers.iherb.android.model.Prescription
import ru.biohackers.iherb.android.utils.ImageRecognizer
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PrescriptionViewModel @Inject constructor(
    private val repository: Repository,
    private val fileManager: FileManager,
    private val imageRecognizer: ImageRecognizer,
) : ViewModel() {

    private val _prescriptions = MutableStateFlow(listOf<Prescription>())
    val prescriptions: StateFlow<List<Prescription>>
        get() = _prescriptions

    init {
        viewModelScope.launch {
            _prescriptions.emit(getInitialPrescriptions())
        }
    }

    fun getInitialPrescriptions(): List<Prescription> {
        return repository.getPrescriptions()
    }

    fun addPrescription(uri: Uri) {
        viewModelScope.launch {
            // show loader
            val file: File = fileManager.savePrescriptionImageFile(uri)
            val string = imageRecognizer.recognizeImage(file)

            val terms = string.split(" ").toSet()
            val bg = repository.getBadGroupsBySynonyms(terms.toList())
            val prescription = Prescription(
                id = -1,
                name = file.name,
                date = LocalDateTime.now(),
                fileLocation = file.path,
                badGroups = bg,
            )
            repository.addPrescription(prescription)

            // hide loader
        }
    }


}
