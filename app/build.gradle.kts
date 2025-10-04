plugins {
    alias(libs.plugins.lazypizza.android.application)
}

android {
    namespace = "com.fomaxtro.lazypizza"
}

dependencies {
    implementation(projects.core.presentation)

    implementation(libs.androidx.core.spashscreen)
}
