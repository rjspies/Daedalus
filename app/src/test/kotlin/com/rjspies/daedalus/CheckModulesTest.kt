package com.rjspies.daedalus

import android.content.Context
import com.rjspies.daedalus.koin.APP_MODULE
import org.junit.jupiter.api.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.KoinTest
import org.koin.test.verify.verify

class CheckModulesTest : KoinTest {
    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun checkAllModules() {
        APP_MODULE.verify(
            extraTypes = listOf(Context::class),
        )
    }
}
