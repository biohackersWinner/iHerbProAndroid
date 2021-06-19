package ru.biohackers.iherb.android.presentation.anketa

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizerOptions
import dagger.hilt.android.AndroidEntryPoint
import ru.biohackers.iherb.android.databinding.AnketaFragmentBinding

@AndroidEntryPoint
class AnketaFragment : Fragment() {

    private var _binding: AnketaFragmentBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("_binding is null")

    private val viewModel: MainViewModel by viewModels()
    val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = AnketaFragmentBinding.inflate(layoutInflater, container, false)
        .apply { _binding = this }.root

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        binding.webview.apply {

            settings.apply {
                javaScriptEnabled = true
                builtInZoomControls = false
                domStorageEnabled = true
            }
            webViewClient = object : WebViewClient() {

                override fun onPageFinished(
                    view: WebView?,
                    url: String?
                ) {
//                    sr.isRefreshing = false
                    // prepareWebApp()
                }
            }
            // loadUrl(getLocalizedUrl())
            loadUrl("https://docs.google.com/forms/d/e/1FAIpQLSecg5nIi-fxW-8ApmjHYJCO2m-YWCYTNonr2WL7HDrHBr469Q/viewform?usp=sf_link")
           //   binding.webview.runJs("\$(\"#mG61Hd > div.freebirdFormviewerViewFormCard.exportFormCard > div > div.freebirdFormviewerViewNavigationNavControls > div.freebirdFormviewerViewNavigationPasswordWarning\").remove()")
        }
    }

}
fun WebView.runJs(js: String) {
    loadUrl("javascript:(function(){$js;})()");
}

//