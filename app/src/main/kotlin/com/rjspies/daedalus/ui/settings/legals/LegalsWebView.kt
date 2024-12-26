package com.rjspies.daedalus.ui.settings.legals

import android.graphics.Color
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kevinnzou.web.WebView
import com.kevinnzou.web.rememberWebViewState
import com.rjspies.daedalus.ui.common.horizontalSpacingM

const val BASE_URL_LEGALS = "https://daedalus-6fd2ac.gitlab.io"

@Composable
fun LegalsWebView(
    url: String,
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
        val webViewState = rememberWebViewState(url)

        AnimatedVisibility(
            visible = webViewState.isLoading,
            modifier = Modifier.fillMaxWidth(),
            enter = expandVertically(),
            exit = shrinkVertically(),
            content = { LinearProgressIndicator() },
        )

        WebView(
            state = webViewState,
            onCreated = {
                it.setBackgroundColor(Color.TRANSPARENT)
                it.clearCache(true)
            },
        )
    }
}
