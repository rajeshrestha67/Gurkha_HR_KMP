package com.gurkha.hr.components

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
actual fun PlatformWebView(modifier: Modifier, url: String) {
    AndroidView(
        modifier = modifier, factory = { ctx ->
            WebView(ctx).apply {
                settings.javaScriptEnabled = true
                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView, url: String) {

                    }
                }
                loadUrl(url)
            }
        },
        update = { webView ->

        }
    )
}