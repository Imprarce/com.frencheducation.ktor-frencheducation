package com.frencheducation.plugins

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        route("/create"){
            post {
                val body = call.receive<String>()
                call.respond(body)
            }
            delete {
                val body = call.receive<String>()
                call.respond(body)
            }
        }
    }
}
