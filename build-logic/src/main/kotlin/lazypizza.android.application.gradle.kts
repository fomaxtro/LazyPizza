import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.android.application")
    kotlin("android")
    id("lazypizza.library.compose")
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_11
    }
}

android {
    compileSdk = libraries.findVersion("app-compileSdk").get().toString().toInt()

    defaultConfig {
        applicationId = libraries.findVersion("app-applicationId").get().toString()
        minSdk = libraries.findVersion("app-minSdk").get().toString().toInt()
        targetSdk = libraries.findVersion("app-targetSdk").get().toString().toInt()
        versionCode = libraries.findVersion("app-versionCode").get().toString().toInt()
        versionName = libraries.findVersion("app-versionName").get().toString()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    "implementation"(libraries.findLibrary("androidx-activity-compose").get())
    "implementation"(libraries.findLibrary("timber").get())
    "implementation"(libraries.findLibrary("koin-android").get())
    test(project)
}