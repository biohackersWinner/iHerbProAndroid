package ru.biohackers.iherb.android.presentation.prescription

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.biohackers.iherb.android.data.Repository
import ru.biohackers.iherb.android.model.Prescription
import javax.inject.Inject

@HiltViewModel
class PrescriptionViewModel @Inject constructor(
    private val repository: Repository
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

    fun addPrescription(uri: Uri?) {

    }

}
