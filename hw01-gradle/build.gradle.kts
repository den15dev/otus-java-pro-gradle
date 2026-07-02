plugins {
    id("java")
}

group = "ru.otus.java.pro"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.guava:guava:33.6.0-jre")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "ru.otus.java.pro.Main"
    }

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    val dependencies = configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) }
    from(dependencies)
}
