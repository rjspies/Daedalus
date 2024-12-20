package com.rjspies.daedalus.legals

import com.rjspies.daedalus.ui.settings.legals.imprint.ENDPOINT_IMPRINT
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class ImprintViewModelTest {
    @Test
    fun `Endpoint should be correct`() {
        ENDPOINT_IMPRINT shouldBe "imprint.html"
    }
}
