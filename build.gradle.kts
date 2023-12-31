val ehcache_version: String by project
val exposed_version: String by project
val h2_version: String by project
val hikaricp_version: String by project
val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm") version "1.9.10"
    id("io.ktor.plugin") version "2.3.3"
}

group = "com.example"
version = "0.0.1"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("com.h2database:h2:$h2_version")
    implementation("com.zaxxer:HikariCP:$hikaricp_version")
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-freemarker-jvm")
    implementation("io.ktor:ktor-server-host-common-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("org.ehcache:ehcache:$ehcache_version")
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")

    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}
