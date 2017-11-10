pluginManagement {
    repositories {
        maven { url = uri("http://dl.bintray.com/kotlin/kotlin-eap-1.2") }
        gradlePluginPortal()
        google()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "com.android.application") {
                useModule("com.android.tools.build:gradle:${requested.version}")
            }
        }
    }
}

rootProject.buildFileName = "build.gradle.kts"
