plugins {
    id("lazypizza.android.library")
    id("lazypizza.library.compose")
}

android {
    buildFeatures {
        compose = true
    }
}

dependencies {
    "implementation"(libraries.findLibrary("androidx-activity-compose").get())
    "implementation"(libraries.findLibrary("koin-compose").get())
    "implementation"(libraries.findBundle("material3-adaptive").get())
}