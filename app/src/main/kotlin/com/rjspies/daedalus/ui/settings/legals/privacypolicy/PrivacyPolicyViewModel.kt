package com.rjspies.daedalus.ui.settings.legals.privacypolicy

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import com.rjspies.daedalus.ui.settings.legals.BASE_URL_LEGALS
import java.io.File

@VisibleForTesting
const val ENDPOINT_PRIVACY_POLICY = "privacy_policy.html"

class PrivacyPolicyViewModel : ViewModel() {
    val url = BASE_URL_LEGALS + File.separator + ENDPOINT_PRIVACY_POLICY
}
