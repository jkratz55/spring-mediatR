import com.jfrog.bintray.gradle.BintrayExtension
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.api.publish.maven.MavenPublication
import java.util.*

plugins {
    kotlin("jvm") version "1.3.70"
    maven
    `maven-publish`
    jacoco
    id("com.jfrog.bintray") version "1.8.5"
    id("org.jetbrains.dokka") version "0.10.1"
}

group = "io.jkratz.springmediatr"
version = "1.1-RELEASE"

repositories {
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation(group = "org.springframework", name = "spring-context", version = "5.1.4.RELEASE")
    implementation(group = "org.slf4j", name = "slf4j-api", version = "1.7.25")
    implementation(group = "javax.validation", name = "validation-api", version = "2.0.1.Final")

    testImplementation(kotlin("test-junit"))
    testImplementation(group = "org.springframework", name = "spring-test", version = "5.1.4.RELEASE")
    testImplementation(group = "org.mockito", name = "mockito-core", version = "2.23.4")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

val sourceJar = task("sourceJar", Jar::class) {
    dependsOn(tasks["classes"])
    classifier = "sources"
    from(sourceSets.main.get().allSource)
}

val javadocJar = task("javadocJar", Jar::class) {
    val javadoc = tasks["dokka"] as DokkaTask
    javadoc.outputFormat = "javadoc"
    javadoc.outputDirectory = "$buildDir/javadoc"
    dependsOn(javadoc)
    classifier = "javadoc"
    from(javadoc.outputDirectory)
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/jkratz55/spring-mediatR")
            credentials {
                username = System.getenv("GITHUB_USERNAME")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
    publications {
        create("mavenJava", MavenPublication::class.java).apply {
            groupId = project.group.toString()
            this.artifactId = artifactId
            version = project.version.toString()
            pom {
                description.set("Implementation of Mediator pattern for JVM and Spring Framework")
                name.set(artifactId)
                url.set("https://github.com/jkratz55/spring-mediatR")
                licenses {
                    license {
                        name.set("Apache License 2.0")
                        url.set("https://opensource.org/licenses/Apache-2.0")
                        distribution.set("repo")
                    }
                }
                developers {
                    developer {
                        id.set("jkratz55")
                        name.set("Joseph Kratz")
                        email.set("joseph.kratz06@gmail.com")
                    }
                }
                scm {
                    url.set("https://github.com/jkratz55/spring-mediatR")
                }
            }

            from(components["java"])
            artifact(sourceJar)
            artifact(javadocJar)
        }
        create("gpr", MavenPublication::class.java).apply {
            groupId = project.group.toString()
            this.artifactId = artifactId
            version = project.version.toString()
            pom {
                description.set("Implementation of Mediator pattern for JVM and Spring Framework")
                name.set(artifactId)
                url.set("https://github.com/jkratz55/spring-mediatR")
                licenses {
                    license {
                        name.set("Apache License 2.0")
                        url.set("https://opensource.org/licenses/Apache-2.0")
                        distribution.set("repo")
                    }
                }
                developers {
                    developer {
                        id.set("jkratz55")
                        name.set("Joseph Kratz")
                        email.set("joseph.kratz06@gmail.com")
                    }
                }
                scm {
                    url.set("https://github.com/jkratz55/spring-mediatR")
                }
            }

           from(components["java"])
            artifact(sourceJar)
            artifact(javadocJar)
        }
    }
}

bintray {
    user = System.getenv("BINTRAY_USER")
    key = System.getenv("BINTRAY_API_KEY")
    pkg(closureOf<BintrayExtension.PackageConfig> {
        repo = "maven-public"
        name = "spring-mediatR"
        setLicenses("Apache-2.0")
        vcsUrl = "https://github.com/jkratz55/spring-mediatR"
        publish = true
        setPublications("mavenJava")
        version(closureOf<BintrayExtension.VersionConfig> {
            this.name = project.version.toString()
            released = Date().toString()
        })
    })
}