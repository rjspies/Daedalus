package com.rjspies.daedalus

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.architecture.KoArchitectureCreator.assertArchitecture
import com.lemonappdev.konsist.api.architecture.Layer
import org.junit.jupiter.api.Test

private val presentationLayer = Layer("Presentation", "com.rjspies.daedalus.presentation..")
private val domainLayer = Layer("Domain", "com.rjspies.daedalus.domain..")
private val dataLayer = Layer("Data", "com.rjspies.daedalus.data..")

class ArchitectureLayerKonsistTest {
    @Test
    fun `Architecture layer have correct dependencies`() {
        Konsist
            .scopeFromProject()
            .assertArchitecture {
                presentationLayer.dependsOn(domainLayer)
                presentationLayer.doesNotDependOn(dataLayer)
                dataLayer.dependsOn(domainLayer)
                dataLayer.doesNotDependOn(presentationLayer)
                domainLayer.dependsOnNothing()
            }
    }
}
