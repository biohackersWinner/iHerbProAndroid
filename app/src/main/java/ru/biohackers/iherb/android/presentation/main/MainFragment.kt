package ru.biohackers.iherb.android.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizerOptions
import dagger.hilt.android.AndroidEntryPoint
import ru.biohackers.iherb.android.R
import ru.biohackers.iherb.android.databinding.MainFragmentBinding

@AndroidEntryPoint
class MainFragment : Fragment() {

//    val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
//        val image = InputImage.fromFilePath(requireContext(), uri)
//        val result = recognizer.process(image)
//            .addOnSuccessListener { visionText: Text ->
//                Log.d("!!!", "${visionText.textBlocks.size}")
//
//                // Task completed successfully
//                // ...
//                val resultText = visionText.text
//                for (block in visionText.textBlocks) {
//                    val blockText = block.text
//                    val blockCornerPoints = block.cornerPoints
//                    val blockFrame = block.boundingBox
//                    for (line in block.lines) {
//                        val lineText: String = line.text
//                        Log.d("!!!", lineText)
//                        val lineCornerPoints = line.cornerPoints
//                        val lineFrame = line.boundingBox
//                        for (element in line.elements) {
//                            val elementText = element.text
//                            val elementCornerPoints = element.cornerPoints
//                            val elementFrame = element.boundingBox
//
//                            Log.d("!!!", elementText)
////                                Log.d("!!!", elementText)
//                        }
//                    }
//                }
//            }
//            .addOnFailureListener { e ->
//                e.printStackTrace()
//                Log.d("!!!", e.localizedMessage)
//                // Task failed with an exception
//                // ...
//            }
//    }

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("_binding is null")

    private val viewModel: MainViewModel by viewModels()
    val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = MainFragmentBinding.inflate(layoutInflater, container, false)
        .apply { _binding = this }.root

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
//
//        binding.button.setOnClickListener {
//            getContent.launch("image/*")
//        }

        binding.button.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_chatFragment)
        }
    }

}
