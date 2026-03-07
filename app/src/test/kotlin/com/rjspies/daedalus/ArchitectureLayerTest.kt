package com.rjspies.daedalus

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses

@AnalyzeClasses(packages = ["com.rjspies.daedalus"])
class ArchitectureLayerTest {
    @ArchTest
    fun `Presentation does not depend on Data`(importedClasses: JavaClasses) {
        noClasses()
            .that().resideInAPackage("com.rjspies.daedalus.presentation..")
            .should().dependOnClassesThat().resideInAPackage("com.rjspies.daedalus.data..")
            .check(importedClasses)
    }

    @ArchTest
    fun `Data does not depend on Presentation`(importedClasses: JavaClasses) {
        noClasses()
            .that().resideInAPackage("com.rjspies.daedalus.data..")
            .should().dependOnClassesThat().resideInAPackage("com.rjspies.daedalus.presentation..")
            .check(importedClasses)
    }

    @ArchTest
    fun `Domain does not depend on Data or Presentation`(importedClasses: JavaClasses) {
        noClasses()
            .that().resideInAPackage("com.rjspies.daedalus.domain..")
            .should().dependOnClassesThat().resideInAnyPackage(
                "com.rjspies.daedalus.data..",
                "com.rjspies.daedalus.presentation..",
            )
            .check(importedClasses)
    }
}
