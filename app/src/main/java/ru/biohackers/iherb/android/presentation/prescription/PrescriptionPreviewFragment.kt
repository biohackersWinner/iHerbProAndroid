package ru.biohackers.iherb.android.presentation.prescription

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import ru.biohackers.iherb.android.databinding.PrescriptionPreviewFragmentBinding
import ru.biohackers.iherb.android.utils.bind
import java.io.File

@AndroidEntryPoint
class PrescriptionPreviewFragment : Fragment() {

    private var _binding: PrescriptionPreviewFragmentBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("_binding is null")

    private val viewModel: PrescriptionPreviewViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = PrescriptionPreviewFragmentBinding.inflate(layoutInflater, container, false)
        .apply { _binding = this }.root

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        bind(viewModel.prescription) { prescription ->
            Glide.with(binding.imageViewPreview)
                .load(Uri.fromFile(File(prescription.fileLocation)))
                .into(binding.imageViewPreview)

        }
    }
}
