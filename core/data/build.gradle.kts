import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.lazypizza.android.library)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.fomaxtro.core.data"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        val properties = gradleLocalProperties(rootDir, providers)

        buildConfigField("String", "API_URL", "\"${properties["API_URL"]}\"")
    }
}

dependencies {
    implementation(projects.core.domain)
    implementation(libs.bundles.ktor)
}