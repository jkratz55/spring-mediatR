import org.gradle.api.tasks.bundling.Jar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jreleaser.model.Active

plugins {
    kotlin("jvm") version "2.0.21"
    id("org.jetbrains.dokka") version "1.9.20"
    id("maven-publish")
    id("org.jreleaser") version "1.15.0" // or the latest version
}

group = "io.github.jkratz55"
version = "2.0.0-RELEASE"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation(group = "org.springframework", name = "spring-context", version = "6.2.0")
    implementation(group = "org.slf4j", name = "slf4j-api", version = "2.0.16")
    implementation(group = "javax.validation", name = "validation-api", version = "2.0.1.Final")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")

    testImplementation(kotlin("test-junit"))
    testImplementation(group = "org.springframework", name = "spring-test", version = "6.2.0")
    testImplementation(group = "org.mockito", name = "mockito-core", version = "5.14.2")
}

tasks.withType<KotlinCompile> {
    compilerOptions.jvmTarget.set(JvmTarget.JVM_21)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

// Create Sources and Javadoc JARs
val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets["main"].allSource)
}

val dokkaJavadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
    from(tasks.dokkaJavadoc)
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            artifact(sourcesJar)
            artifact(dokkaJavadocJar)

            pom {
                name.set("Spring Mediator")
                description.set("A mediator library for Spring Framework")
                url.set("https://github.com/jkratz55/spring-mediator")
                inceptionYear.set("2020")

                licenses {
                    license {
                        name.set("Apache License, Version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.html")
                    }
                }

                developers {
                    developer {
                        id.set("jkratz55")
                        name.set("Joseph Kratz")
                        email.set("4985721+jkratz55@users.noreply.github.com") // Update to your actual email
                    }
                }

                scm {
                    connection.set("scm:git:https://github.com/jkratz55/spring-mediator.git")
                    developerConnection.set("scm:git:ssh://git@github.com:jkratz55/spring-mediator.git")
                    url.set("https://github.com/jkratz55/spring-mediator")
                }
            }
        }
    }

    repositories {
        maven {
            setUrl(layout.buildDirectory.dir("staging-deploy"))
        }
    }
}

jreleaser {
    project {
        name.set("spring-mediator")
        version.set("1.1-RELEASE")
        description.set("A mediator library for Spring Framework")
        copyright.set("2020 Joseph Kratz")
        links{
            homepage.set("https://github.com/jkratz55/spring-mediator")
        }
    }
    signing {
        active.set(Active.ALWAYS)
        passphrase.set(System.getenv("JRELEASER_GPG_PASSPHRASE") ?: "")
        publicKey.set(System.getenv("JRELEASER_GPG_PUBLIC_KEY") ?: "")
        secretKey.set(System.getenv("JRELEASER_GPG_SECRET_KEY") ?: "")
        armored.set(true)
    }
    deploy {
        maven {
            mavenCentral {
                create("sonatype") {
                    setActive("ALWAYS")
                    url = "https://central.sonatype.com/api/v1/publisher"
                    stagingRepository(layout.buildDirectory.dir("staging-deploy").get().toString())
                    username = System.getenv("SONATYPE_USERNAME")
                    password = System.getenv("SONATYPE_PASSWORD")
                    sign.set(true)
                }
            }
        }
    }
    release {
        github {
            token.set(System.getenv("GITHUB_TOKEN") ?: "dummy-github-token")
        }
    }
}

