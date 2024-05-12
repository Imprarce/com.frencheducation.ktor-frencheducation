package com.frencheducation.routes

import com.frencheducation.authentication.JwtService
import com.frencheducation.data.model.SimpleResponse
import com.frencheducation.data.model.user.*
import com.frencheducation.repository.UserRepository
import com.google.gson.GsonBuilder
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File
import java.time.LocalDateTime
import java.util.*

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

            if (checkUser != null) {
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



    post("v1/users/images") {
        val multipart = call.receiveMultipart()
        var fileName: String? = null

        try {
            multipart.forEachPart { partData ->
                when (partData) {
                    is PartData.FormItem -> {
                        if (partData.name == "email") {
                        }
                    }
//
                    is PartData.FileItem -> {
                        fileName = partData.originalFileName ?: "unknown"
                        val file = File("src\\main\\resources\\images\\$fileName")
                        partData.streamProvider().use { input ->
                            file.outputStream().buffered().use { output ->
                                input.copyTo(output)
                            }
                        }
                    }

                    is PartData.BinaryItem -> Unit
                    else -> {}
                }
            }
            val imageUrl = "${Constants.BASE_URL}/$fileName"


            val email = call.request.queryParameters["email"]
            if (email.isNullOrBlank()) {
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Не указан email пользователя"))
                return@post
            }
            try {
                val user = db.findUserByEmail(email)
                if (user == null) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        SimpleResponse(false, "Пользователь с указанным email не найден")
                    )
                    return@post
                }
                db.updateUserImage(user.idUser, imageUrl)
                call.respond(HttpStatusCode.OK, SimpleResponse(true, "Аватар успешно обновлен - $imageUrl"))
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.Conflict, SimpleResponse(false, "Ошибка при обновлении аватара пользователя - ${e.message}")
                )
            }
        } catch (ex: Exception) {
            File("${Constants.USER_IMAGES_PATH}/$fileName").delete()
            call.respond(HttpStatusCode.Conflict, "Возникла какая-то ошибка при загрузке файла - ${ex.message}")
        }
    }


    get("/{name}") {
        val filename = call.parameters["name"]!!
        val file = File("src\\main\\resources\\images\\$filename")
        if(file.exists()) {
            call.respondFile(file)
        }
        else call.respond(HttpStatusCode.NotFound)
    }

    post("v1/users/updateName") {
        try {
            val email = call.request.queryParameters["email"]
            val name = call.request.queryParameters["name"]
            if (email.isNullOrBlank() || name.isNullOrBlank()) {
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Не указан email пользователя"))
                return@post
            }

            val user = db.findUserByEmail(email)
            if (user == null) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    SimpleResponse(false, "Пользователь с указанным email не найден")
                )
                return@post
            }
            db.updateUserName(user.idUser, name)
            call.respond(HttpStatusCode.OK, SimpleResponse(true, "Имя успешно обновлено"))
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.Conflict,
                SimpleResponse(false, e.message ?: "Ошибка при обновлении имени пользователя")
            )
        }
    }

    get("v1/uploaded_images/get") {
        val email = call.request.queryParameters["email"]
        if (email == null) {
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Не указана почта"))
            return@get
        }

        try {
            val user = db.findUserByEmail(email)
            if (user == null) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    SimpleResponse(false, "Пользователь с указанным email не найден")
                )
                return@get
            }
            call.respond(HttpStatusCode.OK, user.imageUrl)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError, SimpleResponse(false, "Ошибка при загрузке файла"))
        }
    }

    get("v1/users/get") {
        val email = call.request.queryParameters["email"]

        if (email.isNullOrBlank()) {
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Вы не ввели почту"))
            return@get
        }

        try {

            val gson = GsonBuilder()
                .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeTypeAdapter())
                .create()

            val checkUser = db.findUserByEmail(email)

            if (checkUser == null) {
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

            val jsonString = gson.toJson(user)
            call.respond(HttpStatusCode.OK, jsonString)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Что-то пошло не так"))
        }
    }

    get("v1/users/name") {
        val email = call.request.queryParameters["email"]

        if (email.isNullOrBlank()) {
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Вы не ввели почту"))
            return@get
        }

        try {
            val user = db.findUserByEmail(email)
            if(user!= null){
                call.respond(HttpStatusCode.OK, user.userName)
            } else {
                call.respond(HttpStatusCode.Conflict,  SimpleResponse(false, "Пользователь не найден"))
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Что-то пошло не так"))
        }
    }
}