val ktorVersion: String by project
val kotlinVersion: String by project
val logbackVersion: String by project
val ktormVersion: String by project
val mysqlVersion: String by project

plugins {
    application
    kotlin("jvm") version "1.6.20"
}

group = "cn.diantongren"
version = "0.0.1"
application {
    mainClass.set("cn.diantongren.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-auth:$ktorVersion")
    implementation("io.ktor:ktor-auth-jwt:$ktorVersion")
    implementation("io.ktor:ktor-jackson:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("org.ktorm:ktorm-core:$ktormVersion")
    implementation("mysql:mysql-connector-java:$mysqlVersion")
    implementation("org.ktorm:ktorm-jackson:$ktormVersion")
    implementation("org.ktorm:ktorm-support-mysql:$ktormVersion")

    testImplementation("io.ktor:ktor-server-tests:$ktorVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
}