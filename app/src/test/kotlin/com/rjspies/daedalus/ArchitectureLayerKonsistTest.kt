package com.rjspies.daedalus

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.architecture.KoArchitectureCreator.assertArchitecture
import com.lemonappdev.konsist.api.architecture.Layer
import org.junit.jupiter.api.Test

private val PRESENTATION_LAYER = Layer("Presentation", "com.rjspies.daedalus.presentation..")
private val DOMAIN_LAYER = Layer("Domain", "com.rjspies.daedalus.domain..")
private val DATA_LAYER = Layer("Data", "com.rjspies.daedalus.data..")

class ArchitectureLayerKonsistTest {
    @Test
    fun `Architecture layer have correct dependencies`() {
        Konsist
            .scopeFromProject()
            .assertArchitecture {
                PRESENTATION_LAYER.dependsOn(DOMAIN_LAYER)
                PRESENTATION_LAYER.doesNotDependOn(DATA_LAYER)
                DATA_LAYER.dependsOn(DOMAIN_LAYER)
                DATA_LAYER.doesNotDependOn(PRESENTATION_LAYER)
                DOMAIN_LAYER.dependsOnNothing()
            }
    }
}
