package com.frencheducation.plugins

import com.frencheducation.authentication.JwtService
import com.frencheducation.authentication.hash
import com.frencheducation.repository.Repository
import com.frencheducation.routes.UserRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {

    val db = Repository()
    val jwtService = JwtService()
    val hashFunction = { s: String -> hash(s) }

    routing {
        UserRoutes(db, jwtService, hashFunction)


    }
}
