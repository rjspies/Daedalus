package com.rjspies.daedalus.legals

import com.rjspies.daedalus.ui.settings.legals.privacypolicy.ENDPOINT_PRIVACY_POLICY
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class PrivacyPolicyViewModelTest {
    @Test
    fun `Endpoint should be correct`() {
        ENDPOINT_PRIVACY_POLICY shouldBe "privacy_policy.html"
    }
}
