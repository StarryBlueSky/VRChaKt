import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.31"
    maven
    signing
}

group = "jp.nephy"
version = "2.0.4"
val ktorVersion = "1.2.0"

repositories {
    mavenCentral()
    jcenter()
    maven("https://dl.bintray.com/kotlin/ktor")
    maven("https://dl.bintray.com/kotlin/kotlinx")
}

dependencies {
    compile(kotlin("stdlib-jdk8"))

    compile("io.ktor:ktor-client-core-jvm:$ktorVersion")
    testCompile("io.ktor:ktor-client-apache:$ktorVersion")
    testCompile("io.ktor:ktor-client-cio:$ktorVersion")
    testCompile("io.ktor:ktor-client-jetty:$ktorVersion")
    testCompile("io.ktor:ktor-client-okhttp:$ktorVersion")
    compile("jp.nephy:jsonkt:4.10")

    compile("io.github.microutils:kotlin-logging:1.6.26")
    testCompile("ch.qos.logback:logback-core:1.2.3")
    testCompile("ch.qos.logback:logback-classic:1.2.3")
    testCompile("org.fusesource.jansi:jansi:1.17.1")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

val uploadArchives: Upload by tasks
uploadArchives.apply {
    repositories {
        withConvention(MavenRepositoryHandlerConvention::class) {
            mavenDeployer {
                beforeDeployment {
                    signing.signPom(this)
                }

                val (username, password) = System.getenv("sonatypeUsername") to System.getenv("sonatypePassword")

                withGroovyBuilder {
                    "snapshotRepository"("url" to "https://oss.sonatype.org/content/repositories/snapshots") {
                        "authentication"("userName" to username, "password" to password)
                    }

                    "repository"("url" to "https://oss.sonatype.org/service/local/staging/deploy/maven2") {
                        "authentication"("userName" to username, "password" to password)
                    }
                }

                pom.project {
                    withGroovyBuilder {
                        "name"("VRChaKt")
                        "packaging"("jar")
                        "artifactId"("vrchakt")
                        "description"("VRChat Web API wrapper for Kotlin.")
                        "url"("https://github.com/NephyProject/VRChaKt")

                        "licenses" {
                            "license" {
                                "name"("MIT")
                                "url"("http://opensource.org/licenses/MIT")
                            }
                        }

                        "developers" {
                            "developer" {
                                "id"("SlashNephy")
                                "name"("Slash Nephy")
                                "email"("admin@nephy.jp")
                            }
                        }

                        "scm" {
                            "url"("https://github.com/NephyProject/VRChaKt")
                            "connection"("scm:git:https://github.com/NephyProject/VRChaKt.git")
                            "developerConnection"("scm:git:git://github.com/NephyProject/VRChaKt.git")
                            "tag"("HEAD")
                        }

                        "issueManagement" {
                            "system"("GitHub Issues")
                            "url"("https://github.com/NephyProject/VRChaKt/issues")
                        }
                    }
                }
            }
        }
    }
}

val javadocJar by tasks.creating(Jar::class) {
    classifier = "javadoc"
    from(tasks["javadoc"])
}

val sourceSets = the<JavaPluginConvention>().sourceSets
val sourcesJar by tasks.creating(Jar::class) {
    classifier = "sources"
    from(sourceSets["main"].java.srcDirs)
}

artifacts {
    withGroovyBuilder {
        "archives"(tasks["jar"], sourcesJar, javadocJar)
    }
}

signing {
    gradle.taskGraph.whenReady {
        isRequired = hasTask("uploadArchives")
    }
    sign(configurations.archives)
}
