package com.rjspies.daedalus.ui.settings.legals

import android.graphics.Color
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.rjspies.daedalus.ui.common.horizontalSpacingM
import java.io.File

private const val BASE_URL = "https://daedalus-6fd2ac.gitlab.io"

@Composable
fun LegalsWebView(
    item: LegalsWebViewItem,
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .horizontalSpacingM()
            .then(modifier),
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = {
                WebView(it).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                    )
                    setBackgroundColor(Color.TRANSPARENT)
                    isVerticalScrollBarEnabled = false
                    webViewClient = WebViewClient()
                }
            },
            update = {
                it.clearCache(true)
                it.loadUrl(BASE_URL + File.separator + item.endpoint)
            },
        )
    }
}

enum class LegalsWebViewItem(val endpoint: String) {
    Imprint("imprint.html"),
    PrivacyPolicy("privacy_policy.html"),
}
