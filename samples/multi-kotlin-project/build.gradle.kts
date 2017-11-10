plugins {
    base
    kotlin("jvm") version "1.2.0-rc-39" apply false
}

allprojects {

    group = "org.gradle.kotlin.dsl.samples.multiproject"

    version = "1.0"

    repositories {
        maven { url = uri("http://dl.bintray.com/kotlin/kotlin-eap-1.2") }
        jcenter()
    }
}

dependencies {
    // Make the root project archives configuration depend on every subproject
    subprojects.forEach {
        archives(it)
    }
}
