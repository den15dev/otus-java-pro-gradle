plugins {
    id("java")
}

group = "ru.otus.java.pro"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.flywaydb:flyway-core:13.4.0")
    implementation("org.slf4j:slf4j-api:2.0.18")
    implementation("ch.qos.logback:logback-classic:1.5.38")
    implementation("com.zaxxer:HikariCP:7.0.2")
}
