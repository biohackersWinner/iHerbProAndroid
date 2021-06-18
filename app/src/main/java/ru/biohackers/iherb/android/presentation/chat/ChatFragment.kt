package ru.biohackers.iherb.android.presentation.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizerOptions
import dagger.hilt.android.AndroidEntryPoint
import ru.biohackers.iherb.android.databinding.ChatFragmentBinding
import ru.biohackers.iherb.android.databinding.MainFragmentBinding
import ru.biohackers.iherb.android.utils.bind

@AndroidEntryPoint
class ChatFragment : Fragment() {

    private var _binding: ChatFragmentBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("_binding is null")

    private val viewModel: ChatViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ChatFragmentBinding.inflate(layoutInflater, container, false)
        .apply { _binding = this }.root

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        bind(viewModel.messages) {

        }
    }

}
