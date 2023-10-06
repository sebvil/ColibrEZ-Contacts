
val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.serialization)
}

group = "com.colibrez.contacts"
version = "0.0.1"

application {
    mainClass.set("com.colibrez.contacts.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/kotlin-js-wrappers") }
}

dependencies {
    implementation(libs.ktor.server.html.builder.jvm)
    implementation(libs.ktor.server.core.jvm)
    implementation(libs.kotlin.css.jvm)
    implementation(libs.ktor.server.content.negotiation.jvm)
    implementation(libs.ktor.serialization.kotlinx.json.jvm)
    implementation(libs.ktor.server.openapi)
    implementation(libs.ktor.server.swagger.jvm)
    implementation(libs.ktor.server.http.redirect.jvm)
    implementation(libs.ktor.server.default.headers.jvm)
    implementation(libs.ktor.server.sessions.jvm)
    implementation(libs.ktor.server.resources)
    implementation(libs.ktor.server.auth.jvm)
    implementation(libs.ktor.server.netty.jvm)
    implementation(libs.logback)
    testImplementation(libs.ktor.server.tests.jvm)
    testImplementation(libs.kotlin.test.junit)
}
