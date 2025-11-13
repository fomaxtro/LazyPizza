import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.lazypizza.android.library)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
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

    room {
        schemaDirectory("$projectDir/schemas")
    }
}

dependencies {
    implementation(projects.core.domain)
    implementation(libs.bundles.ktor)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.bundles.room)
    ksp(libs.room.compiler)
}