plugins {
    id("org.springframework.boot") version "2.7.5"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
    kotlin("jvm") version "1.7.20"
    kotlin("plugin.spring") version "1.7.20"
    kotlin("plugin.jpa") version "1.7.20"
//    id("io.gitlab.arturbosch.detekt") version "1.22.0"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    implementation(group = "org.springframework.boot", name = "spring-boot-starter-actuator")
    implementation(group = "io.micrometer", name = "micrometer-registry-prometheus")
    implementation(group = "org.springframework.boot", name = "spring-boot-starter-web")
    implementation(group = "com.graphql-java", name = "graphql-java")
    implementation(group = "org.springframework.boot", name = "spring-boot-starter-data-jpa")
    implementation(group = "org.postgresql", name = "postgresql")

    // utils
    implementation(group = "io.github.microutils", name = "kotlin-logging", version = "3.0.4")
    implementation(group = "com.fasterxml.jackson.module", name = "jackson-module-kotlin")
    implementation(group = "org.springdoc", name = "springdoc-openapi-ui", version = "1.6.14")
// https://mvnrepository.com/artifact/org.assertj/assertj-core
    implementation("org.assertj:assertj-core:3.24.2")

    // kotlin
    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib-jdk8"))

    // test
    testImplementation(group = "org.junit.jupiter", name = "junit-jupiter-api", version = "5.9.1")
    testImplementation(group = "org.springframework.boot", name = "spring-boot-starter-test")
}

tasks {
    test {
        useJUnitPlatform()
    }
}
