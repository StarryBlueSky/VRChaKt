import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.Coroutines

plugins {
    kotlin("jvm") version "1.2.51"
}

group = "jp.nephy"
version = "1.0-SNAPSHOT"
val ktorVersion = "0.9.3"

repositories {
    mavenCentral()
    maven("https://dl.bintray.com/kotlin/ktor")
    maven("https://dl.bintray.com/kotlin/kotlinx")
}

dependencies {
    compile(kotlin("stdlib-jdk8"))

    compile("io.ktor", "ktor-client-core", ktorVersion)
    compile("io.ktor", "ktor-client-apache", ktorVersion)
    compile("jp.nephy", "jsonkt", "1.5")

    compile("io.github.microutils", "kotlin-logging", "1.5.4")

    testCompile("ch.qos.logback", "logback-core", "1.2.3")
    testCompile("ch.qos.logback", "logback-classic", "1.2.3")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

kotlin {
    experimental.coroutines = Coroutines.ENABLE
}
