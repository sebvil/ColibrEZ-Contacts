package com.colibrez.contacts

import com.colibrez.contacts.plugins.configureHTTP
import com.colibrez.contacts.plugins.configureRouting
import com.colibrez.contacts.plugins.configureSecurity
import com.colibrez.contacts.plugins.configureSerialization
import com.colibrez.contacts.plugins.configureTemplating
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    embeddedServer(Netty, port = 8080, host = "127.0.0.1", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureTemplating()
    configureSerialization()
    configureHTTP()
    configureSecurity()
    configureRouting()
}
