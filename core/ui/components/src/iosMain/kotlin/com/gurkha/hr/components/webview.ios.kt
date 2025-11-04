package com.gurkha.hr.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import platform.Foundation.NSURL
import platform.Foundation.NSURLRequest
import platform.WebKit.WKWebView

@Composable
actual fun PlatformWebView(modifier: Modifier, url: String) {
    val webView = remember { WKWebView() }

    // 2. Load the request when the Composable is first laid out or the URL changes
    LaunchedEffect(url) {
        val nsUrl = NSURL.URLWithString(url)
        if (nsUrl != null) {
            val request = NSURLRequest.requestWithURL(nsUrl)
            webView.loadRequest(request)
        }
    }

    // 3. Embed the native WKWebView using UIKitView
    UIKitView(
        factory = { webView }, // Pass the native view instance
        modifier = modifier
    )
}