plugins {
    alias(libs.plugins.lazypizza.android.application)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.fomaxtro.lazypizza"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(projects.core.presentation)
    implementation(projects.core.data)
    implementation(projects.core.domain)

    implementation(libs.androidx.core.spashscreen)
    implementation(libs.bundles.navigation3)
    implementation(libs.kotlinx.serialization.core)
}
