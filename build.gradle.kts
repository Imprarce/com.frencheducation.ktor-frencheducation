
val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm") version "1.9.23"
    id("io.ktor.plugin") version "2.3.10"
    id ("com.github.johnrengelman.shadow") version "8.1.1"
    id ("java")
}

group = "com.frencheducation"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}


repositories {
    mavenCentral()
}


dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-sessions-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization-gson-jvm")
    implementation("io.ktor:ktor-server-auth-jvm")
    implementation("io.ktor:ktor-server-auth-jwt-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-locations:$ktor_version")
    implementation("io.ktor:ktor-server-http-redirect:$ktor_version")
    implementation("io.ktor:ktor-network-tls-certificates:$ktor_version")
    implementation("io.ktor:ktor-server-hsts:$ktor_version")

    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")

    implementation("org.jetbrains.exposed:exposed-java-time:0.49.0")
    implementation ("org.jetbrains.exposed:exposed-core:0.49.0")
    implementation ("org.jetbrains.exposed:exposed-dao:0.49.0")
    implementation ("org.jetbrains.exposed:exposed-jdbc:0.49.0")
    implementation ("org.postgresql:postgresql:42.2.18")
    implementation ("com.zaxxer:HikariCP:4.0.3")
}


tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
    archiveBaseName.set("main")
    manifest.attributes["Main-Class"] = "io.ktor.server.netty.EngineMain"

    from(sourceSets.main.get().output)
    configurations = listOf(project.configurations.getByName("runtimeClasspath"))
    dependsOn(tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>())
}