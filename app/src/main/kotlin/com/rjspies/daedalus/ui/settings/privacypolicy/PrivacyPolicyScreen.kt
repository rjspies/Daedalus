package com.rjspies.daedalus.ui.settings.privacypolicy

import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.rjspies.daedalus.R
import com.rjspies.daedalus.ui.common.TopAppBar
import com.rjspies.daedalus.ui.common.horizontalSpacingM

@Destination
@Composable
fun PrivacyPolicyScreen(scaffoldPadding: PaddingValues, navigator: DestinationsNavigator) {
    TopAppBar(
        title = stringResource(R.string.settings_legal_item_privacy_policy),
        navigator = navigator,
        modifier = Modifier.padding(bottom = scaffoldPadding.calculateBottomPadding()), // TODO WTF
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .horizontalSpacingM()
                .padding(scaffoldPadding),
        ) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = {
                    WebView(it).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                        )
                        webViewClient = WebViewClient()
                        loadUrl("https://daedalus-6fd2ac.gitlab.io/privacy_policy.html")
                    }
                },
            )
        }
    }
}
