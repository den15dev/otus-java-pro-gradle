plugins {
    id("java")
}

group = "ru.otus.java.pro"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.eclipse.jetty:jetty-server:12.1.14")
    implementation("org.eclipse.jetty.ee10:jetty-ee10-servlet:12.1.14")

    compileOnly("jakarta.servlet:jakarta.servlet-api:6.1.0")

    implementation("org.hibernate.orm:hibernate-core:7.4.11.Final")

    implementation("org.flywaydb:flyway-core:13.4.0")
    implementation("org.flywaydb:flyway-database-postgresql:13.4.0")

    implementation("org.slf4j:slf4j-api:2.0.18")
    implementation("ch.qos.logback:logback-classic:1.5.38")
    implementation("com.zaxxer:HikariCP:7.0.2")

    runtimeOnly("org.postgresql:postgresql:42.7.13")
}