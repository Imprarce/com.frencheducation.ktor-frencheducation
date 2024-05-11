package com.frencheducation

import com.frencheducation.plugins.configureRouting
import com.frencheducation.plugins.configureSecurity
import com.frencheducation.plugins.configureSerialization
import com.frencheducation.repository.DatabaseFactory
import io.ktor.server.application.*


fun Application.module() {

    DatabaseFactory.init()

    configureSecurity()
    configureSerialization()
    configureRouting()

}
//