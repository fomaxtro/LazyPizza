import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.android.build.api.dsl.BuildType

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

    buildTypes {
        val localProperties = gradleLocalProperties(rootDir, providers)

        fun BuildType.configureApi(key: String) {
            buildConfigField("String", "API_URL", "\"${localProperties.getProperty(key)}\"")
        }

        debug {
            configureApi("debug.apiUrl")
        }

        release {
            configureApi("release.apiUrl")
        }

        create("debugRelease") {
            initWith(getByName("release"))
        }
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