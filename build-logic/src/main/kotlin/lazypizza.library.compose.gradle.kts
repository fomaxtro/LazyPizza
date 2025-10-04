plugins {
    kotlin("plugin.compose")
}

dependencies {
    val composeBom = libraries.findLibrary("androidx-compose-bom").get()

    "implementation"(platform(composeBom))
    "implementation"(libraries.findLibrary("androidx-compose-ui").get())
    "implementation"(libraries.findLibrary("androidx-compose-ui-graphics").get())
    "implementation"(libraries.findLibrary("androidx-compose-ui-tooling-preview").get())
    "implementation"(libraries.findLibrary("androidx-compose-material3").get())

    "androidTestImplementation"(platform(composeBom))
    "androidTestImplementation"(libraries.findLibrary("androidx-compose-ui-test-junit4").get())

    "debugImplementation"(libraries.findLibrary("androidx-compose-ui-tooling").get())
    "debugImplementation"(libraries.findLibrary("androidx-compose-ui-test-manifest").get())
}