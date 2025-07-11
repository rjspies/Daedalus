import org.gradle.internal.jvm.inspection.JvmVendor
import org.gradle.jvm.toolchain.internal.DefaultJvmVendorSpec

plugins {
    alias(libs.plugins.comAndroidApplication)
    alias(libs.plugins.orgJetbrainsKotlinAndroid)
    alias(libs.plugins.comGoogleDevtoolsKsp)
    alias(libs.plugins.ioGitlabArturboschDetekt)
    alias(libs.plugins.orgJmailenKotlinter)
    alias(libs.plugins.orgJetbrainsKotlinPluginCompose)
    alias(libs.plugins.deMannodermausAndroidJunit5)
    alias(libs.plugins.orgJetbrainsKotlinPluginSerialization)
}

android {
    namespace = libs.versions.namespace.get()
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = libs.versions.namespace.get()
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = generateVersionCode()
        versionName = libs.versions.versionName.get()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            storeFile = file("keystore/release.jks")
            storePassword = System.getenv("DAEDALUS_STORE_PASSWORD")
            keyAlias = System.getenv("DAEDALUS_SIGNING_KEY")
            keyPassword = System.getenv("DAEDALUS_SIGNING_KEY_PASSWORD")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            ndk.debugSymbolLevel = "FULL"
            signingConfig = signingConfigs["release"]
        }

        debug {
            applicationIdSuffix = ".debug"
            isDebuggable = true
            isCrunchPngs = false
            signingConfig = signingConfigs["debug"]
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(libs.versions.java.get()))
        vendor.set(DefaultJvmVendorSpec.of(JvmVendor.fromString(libs.versions.javaVendor.get()).knownVendor))
    }

    compilerOptions {
        allWarningsAsErrors.set(true)
    }
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

dependencies {
    implementation(libs.comJakewhartonTimer.timber)
    implementation(libs.androidxActivity.activityCompose)
    implementation(libs.androidxComposeUi.ui)
    implementation(libs.androidxCore.coreKtx)
    implementation(libs.androidxCore.coreSplashscreen)
    implementation(libs.androidxLifecycle.lifecycleRuntimeKtx)
    implementation(libs.androidxComposeMaterial3.material3)
    implementation(libs.ioInsertKoin.koinAndroidxCompose)
    implementation(libs.comPatrykandpatrickVico.composeM3)
    implementation(libs.androidxRoom.roomKtx)
    implementation(libs.androidxRoom.roomRuntime)
    implementation(libs.androidxConstraintlayout.constraintlayoutCompose)
    implementation(libs.devChrisbanesHaze.hazeAndroid)
    implementation(libs.devChrisbanesHaze.hazeMaterials)
    implementation(libs.androidxComposeMaterial.materialIconsExtended)
    implementation(libs.ioInsertKoin.koinAndroid)
    implementation(libs.comGoogleAndroidMaterial.material)
    implementation(libs.androidxComposeUi.uiToolingPreview)
    implementation(libs.androidxNavigation.navigationCompose)
    implementation(libs.comAdevintaSpark.spark)
    debugImplementation(libs.androidxComposeUi.uiTooling)
    ksp(libs.androidxRoom.roomCompiler)
    testImplementation(libs.ioInsertKoin.koinTestJunit4)
    testImplementation(libs.ioKotest.kotestAssertionsCore)
    testImplementation(libs.orgJunitJupiter.junitJupiterApi)
    testImplementation(libs.comLemonappdev.konsist)
    testRuntimeOnly(libs.orgJunitJupiter.junitJupiterEngine)
}

fun generateVersionCode(): Int {
    val executionOutput = providers.exec {
        commandLine("git", "rev-list", "--count", "HEAD")
    }
    val commitCount = executionOutput.standardOutput.asText.get().trim().toInt()
    val offset = libs.versions.versionCodeOffset.get().toInt()
    val versionCode = commitCount + offset
    logger.debug("Generating version code = $versionCode")
    return versionCode
}

tasks {
    register("version") {
        doLast {
            println(libs.versions.versionName.get())
        }
    }

    register("versionCode") {
        doLast {
            println(generateVersionCode())
        }
    }

    withType<Test>().configureEach {
        useJUnitPlatform()
    }
}
