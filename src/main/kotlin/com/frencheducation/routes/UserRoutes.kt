package com.frencheducation.routes

import com.frencheducation.authentication.JwtService
import com.frencheducation.data.model.user.LoginRequest
import com.frencheducation.data.model.user.RegisterRequest
import com.frencheducation.data.model.SimpleResponse
import com.frencheducation.data.model.user.FindRequest
import com.frencheducation.data.model.user.User
import com.frencheducation.repository.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.routing.post
import java.time.LocalDateTime

//const val API_VERSION = "/v1"
//const val USERS = "$API_VERSION/users"
//const val REGISTER_REQUEST = "$USERS/register"
//const val LOGIN_REQUEST = "$USERS/login"

fun Route.UserRoutes(
    db: UserRepository,
    jwtService: JwtService,
    hashFunction: (String) -> String
) {
    post("v1/users/register") {
        val registerRequest = try {
            call.receive<RegisterRequest>()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Вы пропустили какие-то поля"))
            return@post
        }

        try {
            val checkUser = db.findUserByEmail(registerRequest.email)

            if(checkUser != null){
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Данная почта занята почта"))
                return@post
            }

            val user = User(
                idUser = 0,
                email = registerRequest.email,
                hashPassword = hashFunction(registerRequest.password),
                userName = registerRequest.email,
                imageUrl = "",
                dateCreateAcc = LocalDateTime.now()
            )

            db.addUser(user)
            call.respond(HttpStatusCode.OK, SimpleResponse(true, jwtService.generateToken(user)))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Что-то пошло не так"))
        }
    }

    post("v1/users/login") {
        val loginRequest = try {
            call.receive<LoginRequest>()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Вы пропустили какие-то поля"))
            return@post
        }

        try {
            val user = db.findUserByEmail(loginRequest.email)

            if (user == null) {
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Неправильная почта"))
            } else {

                if (user.hashPassword == hashFunction(loginRequest.password)) {
                    call.respond(HttpStatusCode.OK, SimpleResponse(true, jwtService.generateToken(user)))
                } else {
                    call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Неверный пароль"))
                }

            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Что-то пошло не так"))
        }
    }

    get("v1/users/get") {
        val findRequest = try {
            call.receive<FindRequest>()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Вы не ввели почту"))
            return@get
        }

        try {
            val checkUser = db.findUserByEmail(findRequest.email)

            if(checkUser == null){
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Пользователя не существует"))
                return@get
            }

            val user = User(
                idUser = checkUser.idUser,
                email = checkUser.email,
                hashPassword = checkUser.hashPassword,
                userName = checkUser.userName,
                imageUrl = checkUser.imageUrl,
                dateCreateAcc = checkUser.dateCreateAcc
            )
            call.respond(HttpStatusCode.OK, SimpleResponse(true, jwtService.generateToken(user)))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Что-то пошло не так"))
        }
    }
}