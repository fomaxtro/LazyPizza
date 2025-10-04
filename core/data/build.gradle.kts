plugins {
    alias(libs.plugins.lazypizza.android.library)
}

android {
    namespace = "com.fomaxtro.core.data"
}

dependencies {
    implementation(projects.core.domain)
}