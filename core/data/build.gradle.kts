plugins {
    alias(libs.plugins.lazypizza.android.library)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.fomaxtro.core.data"
}

dependencies {
    implementation(projects.core.domain)
    implementation(libs.bundles.ktor)
}