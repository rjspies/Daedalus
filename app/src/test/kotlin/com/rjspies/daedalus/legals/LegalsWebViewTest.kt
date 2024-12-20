package com.rjspies.daedalus.legals

import com.rjspies.daedalus.ui.settings.legals.BASE_URL_LEGALS
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class LegalsWebViewTest {
    @Test
    fun `Base url should be correct`() {
        BASE_URL_LEGALS shouldBe "https://daedalus-6fd2ac.gitlab.io"
    }
}
