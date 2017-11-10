import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    base
    kotlin("jvm") version "1.2.0-rc-39" apply false
}

allprojects {

    group = "org.gradle.kotlin.dsl.samples.multiprojectci"

    version = "1.0"

    repositories {
        maven { url = uri("http://dl.bintray.com/kotlin/kotlin-eap-1.2") }
        jcenter()
    }
}

// Configure all KotlinCompile tasks on each sub-project
subprojects {

    tasks.withType<KotlinCompile> {
        println("Configuring $name in project ${project.name}...")
        kotlinOptions {
            suppressWarnings = true
        }
    }
}

dependencies {
    // Make the root project archives configuration depend on every subproject
    subprojects.forEach {
        archives(it)
    }
}
