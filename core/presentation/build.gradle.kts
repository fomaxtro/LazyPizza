plugins {
    alias(libs.plugins.lazypizza.feature.ui)
}

android {
    namespace = "com.fomaxtro.core.presentation"
}

dependencies {
    implementation(projects.core.domain)
}