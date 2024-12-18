package com.rjspies.daedalus.ui.settings.legals.imprint

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import com.rjspies.daedalus.ui.settings.legals.BASE_URL_LEGALS
import java.io.File

@VisibleForTesting
const val ENDPOINT = "imprint.html"

class ImprintViewModel : ViewModel() {
    val url = BASE_URL_LEGALS + File.separator + ENDPOINT
}
