plugins {
    alias(libs.plugins.lazypizza.android.application)
    alias(libs.plugins.lazypizza.library.compose)
}

android {
    namespace = "com.fomaxtro.lazypizza"

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.activity.compose)
}
