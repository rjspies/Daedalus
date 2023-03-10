import nl.littlerobots.vcu.plugin.versionCatalogUpdate

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.littlerobots) apply true
    alias(libs.plugins.benManes.versions) apply true
}

versionCatalogUpdate {
    sortByKey.set(false)
    keep {
        keepUnusedVersions.set(true)
    }
}
