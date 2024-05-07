package com.frencheducation

import com.frencheducation.plugins.configureRouting
import com.frencheducation.plugins.configureSecurity
import com.frencheducation.plugins.configureSerialization
import com.frencheducation.repository.DatabaseFactory
import io.ktor.server.application.*
import io.ktor.server.locations.*


fun Application.module() {
    install(Locations)
    DatabaseFactory.init()

    configureSecurity()
    configureSerialization()
    configureRouting()

}
//