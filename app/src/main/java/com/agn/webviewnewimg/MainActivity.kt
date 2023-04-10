package com.agn.webviewnewimg

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.activity.result.contract.ActivityResultContracts


abstract class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var uploadMassage: ValueCallback<Array<Uri>>
    private lateinit var uploadMessageAboveL: ValueCallback<Array<Uri>>

    private val fileChooserLauncher = registerForActivityResult(ActivityResultContracts
        .StartActivityForResult()){ result ->
        if (result.resultCode == Activity.RESULT_OK){
            val resultData: Intent? = result.data
            val resultUri = resultData?.data
            if (resultUri != null){
                uploadMessageAboveL?.onReceiveValue(arrayOf(resultUri))
//                uploadMessageAboveL = null
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        webView = findViewById(R.id.webView)
        webView.settings.allowContentAccess = true

        webView.webChromeClient = object : WebChromeClient() {
            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                uploadMassage = filePathCallback ?: return false
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                if (packageManager.resolveActivity(intent,0) != null){
                    uploadMessageAboveL = uploadMassage
                    fileChooserLauncher.launch(intent)
                }
                return true
            }
        }
        webView.loadUrl("https://www.pixect.com/ru/")
    }
}