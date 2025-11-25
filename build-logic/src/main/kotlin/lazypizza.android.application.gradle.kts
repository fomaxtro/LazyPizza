import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.io.FileInputStream
import java.util.Properties

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

    val keystorePropertiesFile = rootProject.file("keystore.properties")
    val keystoreProperties = Properties().apply {
        load(FileInputStream(keystorePropertiesFile))
    }

    signingConfigs {
        create("release") {
            storeFile = file(keystoreProperties.getProperty("storeFile"))
            storePassword = keystoreProperties.getProperty("storePassword")
            keyAlias = keystoreProperties.getProperty("keyAlias")
            keyPassword = keystoreProperties.getProperty("keyPassword")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        create("debugRelease") {
            initWith(getByName("release"))

            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false
            signingConfig = signingConfigs.getByName("release")

            matchingFallbacks += "debug"
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
    "implementation"(libraries.findLibrary("koin-compose").get())
    test(project)
}