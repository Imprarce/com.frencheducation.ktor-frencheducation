package com.frencheducation.routes

import com.frencheducation.data.model.SimpleResponse
import com.frencheducation.data.model.module_progress.ModuleProgress
import com.frencheducation.repository.ModuleProgressRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.ModuleProgressRoutes(
    db: ModuleProgressRepository
) {
    post("v1/module_progress/create") {
        val moduleProgress = try {
            call.receive<ModuleProgress>()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Пропущены необходимые параметры"))
            return@post
        }

        try {
            db.addModuleProgress(moduleProgress)
            call.respond(HttpStatusCode.OK, SimpleResponse(true, "Прогресс модуля успешно добавлен"))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Возникла какая-то проблема"))
        }
    }

    post("v1/module_progress/update") {
        val moduleProgress = try {
            call.receive<ModuleProgress>()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Пропущены необходимые параметры"))
            return@post
        }

        try {
            db.updateModuleProgress(moduleProgress, moduleProgress.idModule, moduleProgress.idUser)
            call.respond(HttpStatusCode.OK, SimpleResponse(true, "Прогресс модуля успешно обновлен"))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Возникла какая-то проблема"))
        }
    }

    get("v1/module_progress/get") {
        try {
            val idUser = call.request.queryParameters["id_user"]
            val idModule = call.request.queryParameters["id_module"]

            if (idUser.isNullOrBlank()) {
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Пользователь не найден"))
                return@get
            }

            if (idModule.isNullOrBlank()) {
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Модуль не найден"))
                return@get
            }


            val checkModuleProgress = db.getModuleProgress(idUser.toInt(), idModule.toInt())

            if (checkModuleProgress == null) {
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Прогресс не найден"))
                return@get
            }

            val moduleProgress = ModuleProgress(
                idModule = checkModuleProgress.idModule,
                idUser = checkModuleProgress.idUser,
                progress = checkModuleProgress.progress
            )
            call.respond(HttpStatusCode.OK, moduleProgress)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, "Прогресс не найден")
        }
    }

}