plugins {
    alias(libs.plugins.lazypizza.android.application)
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

    implementation(libs.androidx.core.spashscreen)
}
