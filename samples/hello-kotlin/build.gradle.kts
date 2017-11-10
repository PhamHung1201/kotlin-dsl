plugins {
    application
    kotlin("jvm") version "1.2.0-rc-39"
}

application {
    mainClassName = "samples.HelloWorldKt"
}

dependencies {
    compile(kotlin("stdlib"))
}

repositories {
    maven { url = uri("http://dl.bintray.com/kotlin/kotlin-eap-1.2") }
    jcenter()
}
