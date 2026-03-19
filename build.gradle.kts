plugins {
    id("java")
}

group = "dev.faceless"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.code.gson:gson:2.13.2")
}

val libsDir = file("/home/faceless/Documents/Libraries/")

tasks.jar {
    destinationDirectory.set(libsDir)
}

val sourcesJar by tasks.registering(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles a jar with source files"
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allJava)
    destinationDirectory.set(libsDir)
}

tasks.build {
    dependsOn(tasks.jar)
    dependsOn(sourcesJar)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}