package com.example.questionnairecommunityconnectv2

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import androidx.activity.ComponentActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)

        val webView: WebView = findViewById(R.id.webView)
        val survey1Btn: Button = findViewById(R.id.survey_1_btn)
        val survey2Btn: Button = findViewById(R.id.survey_2_btn)

        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true // Enable local storage
        webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW


        // Ensure pages open inside the app
        webView.webViewClient = WebViewClient()
        webView.loadUrl("file:///android_asset/homepage.html")

        survey1Btn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("survey_id", "1")
            startActivity(intent)
        }

        survey2Btn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("survey_id", "2")
            startActivity(intent)
        }
    }
}