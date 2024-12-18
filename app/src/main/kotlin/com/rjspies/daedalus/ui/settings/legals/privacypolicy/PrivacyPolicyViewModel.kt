package com.rjspies.daedalus.ui.settings.legals.privacypolicy

import androidx.lifecycle.ViewModel
import com.rjspies.daedalus.ui.settings.legals.BASE_URL_LEGALS
import java.io.File

class PrivacyPolicyViewModel : ViewModel() {
    val url = BASE_URL_LEGALS + File.separator + "imprint.html"
}
