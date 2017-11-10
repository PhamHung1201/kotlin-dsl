pluginManagement {
    repositories {
        maven { url = uri("http://dl.bintray.com/kotlin/kotlin-eap-1.2") }
        gradlePluginPortal()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "kotlin2js") {
                useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:${requested.version}")
            }
        }
    }
}
