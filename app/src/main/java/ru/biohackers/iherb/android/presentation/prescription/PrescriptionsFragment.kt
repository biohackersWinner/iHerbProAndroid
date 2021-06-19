package ru.biohackers.iherb.android.presentation.prescription

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.biohackers.iherb.android.databinding.PrescriptionsFragmentBinding
import ru.biohackers.iherb.android.utils.bind

@AndroidEntryPoint
class PrescriptionsFragment : Fragment() {

    val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        viewModel.addPrescription(uri)
    }

    private var _binding: PrescriptionsFragmentBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("_binding is null")

    private val viewModel: PrescriptionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = PrescriptionsFragmentBinding.inflate(layoutInflater, container, false)
        .apply { _binding = this }.root

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        bind(viewModel.prescriptions) {

        }

        binding.buttonAddPrescription.setOnClickListener {
            getContent.launch("image/*")
        }
    }



}


