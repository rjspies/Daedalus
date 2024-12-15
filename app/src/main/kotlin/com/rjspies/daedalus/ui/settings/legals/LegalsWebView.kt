package com.rjspies.daedalus.ui.settings.legals

import android.graphics.Color
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kevinnzou.web.WebView
import com.kevinnzou.web.rememberWebViewState
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
        val webViewState = rememberWebViewState(BASE_URL + File.separator + item.endpoint)
        WebView(
            state = webViewState,
            onCreated = {
                it.clearCache(true)
            },
        )
    }
}

enum class LegalsWebViewItem(val endpoint: String) {
    Imprint("imprint.html"),
    PrivacyPolicy("privacy_policy.html"),
}
