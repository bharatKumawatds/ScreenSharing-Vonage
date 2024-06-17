package com.app.screensharevonage

import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app.sharingscreen.ShareScreen

class MainActivity : AppCompatActivity() {
    var main_content:ConstraintLayout ?= null
    var shareScreen:ShareScreen ?= null
    var webViewContainer:WebView ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        main_content = findViewById(R.id.main)
        webViewContainer = findViewById(R.id.webView)

        webViewContainer!!.setWebViewClient(WebViewClient())
        val webSettings: WebSettings = webViewContainer!!.getSettings()
        webSettings.javaScriptEnabled = true
        webViewContainer!!.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        webViewContainer!!.loadUrl("https://www.tokbox.com")
        shareScreen = ShareScreen.Builder()
            .activity(this@MainActivity)
            .context(this)
            .contentView(main_content!!)
            .API_KEY("47934831")
            .SESSION_ID("1_MX40NzkxMzgzMX5-MTcxODI2MTQ5OTk4N35OOHVEVTZnb1BNQ0dUUWFRdmVRdTFOaDB-fn4")
            .TOKEN("T1==cGFydG5lcl9pZD00LzkxOzgzMSZzaWc9ZGVjNzIyMmU1NzI3ZWUyMzRhZTJmNjAxNjVhOTQ4Y2FkMGI5MTA0MzpzZXNzaW9uX2lkPTFfTVg0ME56a3hNemd6TVg1LU1UY3hPREkyTVRRNU9UazROMzVPT0hWRVZUWm5iM05CUTBkVVVXRlJkbVZSZFRGT2FEQi1mbjQmY3JlYXRlX3RpbWU9MTcxODI2MTUwMCZub25jZT0wLjc4MzQ1Mjg3OTI1Mjg3Nzcmcm9sZT1tb2RlcmF0b3ImZXhwaXJlX3RpbWU9MTcyMDg1MzUwMCZpbml0aWFsX2xheW91dF9jbGFzc19saXN0PQ==")
            .build()
        shareScreen!!.initializeShareScreen()

    }

    override fun onResume() {
        super.onResume()
        shareScreen!!.connectSession()

    }

    override fun onPause() {
        super.onPause()
        shareScreen!!.disconnectSession()
    }
}