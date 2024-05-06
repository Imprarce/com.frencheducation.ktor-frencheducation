package com.frencheducation.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.frencheducation.authentication.JwtService
import com.frencheducation.repository.UserRepository
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun Application.configureSecurity() {
    data class MySession(val count: Int = 0)
    install(Sessions) {
        cookie<MySession>("MY_SESSION") {
            cookie.extensions["SameSite"] = "lax"
        }
    }
    // Please read the jwt property from the config file if you are using EngineMain
    val jwtAudience = "jwt-audience"
    val jwtDomain = "https://jwt-provider-domain/"
    val jwtRealm = "FrenchEducation Server"
    val jwtSecret = "secret"
    authentication {
        jwt("jwt") {
            verifier(JwtService().verifier)
            realm = jwtRealm
            validate { credential ->
                val payload = credential.payload
                val email = payload.getClaim("email").asString()
                val user = UserRepository().findUserByEmail(email)
                user
            }
        }
    }
    routing {
        get("/session/increment") {
                val session = call.sessions.get<MySession>() ?: MySession()
                call.sessions.set(session.copy(count = session.count + 1))
                call.respondText("Counter is ${session.count}. Refresh to increment.")
            }
    }
}
