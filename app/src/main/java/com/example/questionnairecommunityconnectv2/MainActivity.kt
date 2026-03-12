package com.example.questionnairecommunityconnectv2

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import okhttp3.*


class MainActivity : ComponentActivity() {
    private lateinit var webView: WebView
    private val client = OkHttpClient()

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the WebView
        val webView: WebView = findViewById(R.id.webView)

        val formButton: Button = findViewById(R.id.button_form)
        val dataButton: Button = findViewById(R.id.button_data)
        val questionnaireButton: Button = findViewById(R.id.button_questionnaire)

        // Configure WebView settings
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true // Enable local storage
        webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW


        // Ensure pages open inside the app
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                if (url == "file:///android_asset/landing_page.html") {
                    injectIntentData(webView)
                }
            }
        }

        webView.loadUrl("file:///android_asset/landing_page.html")


        formButton.setOnClickListener {
            if (isInternetAvailable()) {
                Toast.makeText(this, "Loading Form", Toast.LENGTH_SHORT).show()
                val currentDateTime =
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))
                if (intent.getStringExtra("survey_id") == "1") {
                    val formUrl =
                        "https://docs.google.com/forms/d/e/1FAIpQLScKgjdnZb0J8QmLD0z9ZfcbxdBuzk4ykUV63V1fFN9psnl4kw/viewform?usp=pp_url&entry.1752492738=$currentDateTime"
                    webView.loadUrl(formUrl)
                } else if (intent.getStringExtra("survey_id") == "2") {
                    val formUrl =
                        "https://docs.google.com/forms/d/e/1FAIpQLSf9YKKAU5hTG0sK3ttMTNAkQxTJEgYqZRqfpGbNIPCPWYqbJw/viewform?usp=pp_url&entry.232423121=$currentDateTime"
                    webView.loadUrl(formUrl)
                }
            } else {
                Toast.makeText(this, "Internet not available", Toast.LENGTH_SHORT).show()
            }
        }

        dataButton.setOnClickListener {
            if (isInternetAvailable()) {
                Toast.makeText(this, "Loading Data", Toast.LENGTH_SHORT).show()
                if (intent.getStringExtra("survey_id") == "1") {
                    val dataUrl =
                        "https://docs.google.com/spreadsheets/d/1M9XgKWxLUJsR0HdMYzZq8og7RZtkjswgQANVHWOtpJc/edit?usp=sharing"
                    webView.loadUrl(dataUrl)
                } else if (intent.getStringExtra("survey_id") == "2") {
                    val dataUrl =
                        "https://docs.google.com/spreadsheets/d/1O2CX3jza_ghFMdMlAdKT9PKMS2df2Lq5_ill6OkAgM4/edit?usp=sharing"
                    webView.loadUrl(dataUrl)
                }
            } else {
                Toast.makeText(this, "Internet not available", Toast.LENGTH_SHORT).show()
            }
        }

        questionnaireButton.setOnClickListener {
            Toast.makeText(this, "Loading Questionnaire", Toast.LENGTH_SHORT).show()

            // Load the local PDF.js viewer from assets
            val pdfViewerUrl = "file:///android_asset/questionnaire.html"
            webView.loadUrl(pdfViewerUrl)
        }
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // Get the current network
        val activeNetwork: Network? = connectivityManager.activeNetwork

        // Get network capabilities
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)

        // Check if network is available and has internet access
        return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun injectIntentData(webView: WebView) {
        val surveyId = intent.getStringExtra("survey_id") ?: "No ID Provided"
        val username = intent.getStringExtra("username") ?: "Guest"

        val jsCode = """
            window.receiveIntentData('$surveyId', '$username');
        """.trimIndent()

        webView.evaluateJavascript(jsCode, null)
    }
}

