package ru.biohackers.iherb.android.presentation.prescription

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import org.threeten.bp.format.DateTimeFormatter
import ru.biohackers.iherb.android.databinding.PrescriptionItemBinding
import ru.biohackers.iherb.android.databinding.PrescriptionsFragmentBinding
import ru.biohackers.iherb.android.model.BadGroup
import ru.biohackers.iherb.android.model.Prescription
import ru.biohackers.iherb.android.utils.bind
import java.io.File


@AndroidEntryPoint
class PrescriptionsFragment : Fragment() {

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri ->
            viewModel.addPrescription(uri)
        }

    private var imageUri: Uri? = null
    private val takePicture =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { imageSaved: Boolean ->
            imageUri?.let { viewModel.addPrescription(it) }
        }

    private var _binding: PrescriptionsFragmentBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("_binding is null")
    private val viewModel: PrescriptionViewModel by viewModels()

    private val prescriptionAdapter = PrescriptionAdapter {
        findNavController().navigate(
            PrescriptionsFragmentDirections.actionPrescriptionFragmentToPrescriptionPreviewFragment(
                it
            )
        )
    }

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

        binding.recyclerPrescription.adapter = prescriptionAdapter
        binding.buttonAddPrescriptionPicture.setOnClickListener {
            getContent.launch("image/*")
        }
        binding.buttonAddPrescriptionCamera.setOnClickListener {
//            val imagePath = File(requireContext().filesDir, "images")
//            val newFile = File(imagePath, generateFileName("jpg"))
//            val imageUri =
//                getUriForFile(requireContext(), "ru.biohackers.iherb.fileprovider", newFile)
//            takePicture.launch(imageUri)
            Toast.makeText(requireContext(), "В разработке", Toast.LENGTH_SHORT).show()
        }

        bind(viewModel.prescriptions) {
            prescriptionAdapter.submitList(it)
        }
    }
}

class PrescriptionAdapter(
    val onPreviewShowClickListener: (Int) -> Any
) :
    ListAdapter<Prescription, PrescriptionAdapter.PrescriptionViewHolder>(WeekDiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PrescriptionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PrescriptionViewHolder(
            PrescriptionItemBinding.inflate(inflater, parent, false),
            onPreviewShowClickListener
        )
    }

    override fun onBindViewHolder(holder: PrescriptionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PrescriptionViewHolder(val binding: PrescriptionItemBinding,
                                 val onPreviewShowClickListener: (Int) -> Any) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Prescription) {
            binding.textViewDate.text = DATE_FORMATTER.format(item.date)
            binding.textViewDescription.text = "Распознано ${item.badGroups.size} позиций:" +
                    item.badGroups.fold("") { acc: String, bg: BadGroup ->
                        "$acc\n - ${bg.title}"
                    }
            Glide.with(binding.imageViewPreview)
                .load(Uri.fromFile(File(item.fileLocation)))
                .centerCrop()
                .into(binding.imageViewPreview)

            binding.imageViewPreview.setOnClickListener {
                onPreviewShowClickListener(item.id)
            }
        }
    }

    companion object {
        val DATE_FORMATTER = DateTimeFormatter.ofPattern("d MMM yyyy HH:mm")
    }
}

class WeekDiffCallback : DiffUtil.ItemCallback<Prescription>() {

    override fun areItemsTheSame(
        oldItem: Prescription,
        newItem: Prescription
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Prescription,
        newItem: Prescription
    ): Boolean {
        return oldItem == newItem
    }
}
