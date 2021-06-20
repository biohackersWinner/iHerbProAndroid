package ru.biohackers.iherb.android.presentation.prescription

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import ru.biohackers.iherb.android.data.Repository
import ru.biohackers.iherb.android.model.Prescription
import javax.inject.Inject

@HiltViewModel
class PrescriptionPreviewViewModel @Inject constructor(
    private val repository: Repository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val prescriptionId: Int by lazy { savedStateHandle["prescriptionId"]!! }

    private val _prescription = MutableSharedFlow<Prescription>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val prescription: SharedFlow<Prescription>
        get() = _prescription

    init {
        viewModelScope.launch {
            repository.getPrescription(prescriptionId)?.let {
                _prescription.emit(it)
            }
        }
    }

}
