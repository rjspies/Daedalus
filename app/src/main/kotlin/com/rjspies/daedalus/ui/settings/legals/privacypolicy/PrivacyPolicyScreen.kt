package com.rjspies.daedalus.ui.settings.legals.privacypolicy

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.rjspies.daedalus.R
import com.rjspies.daedalus.ui.common.TopAppBar
import com.rjspies.daedalus.ui.settings.legals.LegalsWebView
import com.rjspies.daedalus.ui.settings.legals.LegalsWebViewItem

@Destination
@Composable
fun PrivacyPolicyScreen(scaffoldPadding: PaddingValues, navigator: DestinationsNavigator) {
    TopAppBar(
        title = stringResource(R.string.settings_legal_item_imprint),
        navigator = navigator,
        modifier = Modifier.padding(bottom = scaffoldPadding.calculateBottomPadding()), // TODO WTF
    ) {
        LegalsWebView(
            item = LegalsWebViewItem.PrivacyPolicy,
            modifier = Modifier.padding(scaffoldPadding),
        )
    }
}
