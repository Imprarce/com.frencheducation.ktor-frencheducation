package com.frencheducation.routes

import com.frencheducation.data.model.SimpleResponse
import com.frencheducation.data.model.module.ModuleItem
import com.frencheducation.data.model.task.Task
import com.frencheducation.repository.ModuleRepository
import com.frencheducation.repository.TaskRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.ModuleRoutes(
    db: ModuleRepository
) {
    post("v1/module/create") {
        val module = try {
            call.receive<ModuleItem>()
        } catch (e: Exception){
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Пропущены необходимые параметры"))
            return@post
        }

        try {
            db.addModule(module)
            call.respond(HttpStatusCode.OK, SimpleResponse(true, "Модуль успешно добавлен"))
        } catch (e: Exception){
            call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Возникла какая-то проблема"))
        }
    }

    post("v1/module/update"){
        val module = try {
            call.receive<ModuleItem>()
        } catch (e: Exception){
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Пропущены необходимые параметры"))
            return@post
        }

        try {
            db.updateModule(module, module.idModule)
            call.respond(HttpStatusCode.OK, SimpleResponse(true, "Модуль успешно обновлен"))
        } catch (e: Exception){
            call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Возникла какая-то проблема"))
        }
    }

    delete("v1/module/delete") {
        val moduleId = try {
            call.request.queryParameters["id"]!!
        } catch (e: Exception){
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "id написан неверно"))
            return@delete
        }

        try{
            db.deleteModule(moduleId.toInt())
            call.respond(HttpStatusCode.OK, SimpleResponse(true, "Модуль успешно удален"))
        } catch (e: Exception){
            call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Возникла какая-то проблема"))
        }
    }

    get("v1/module/get") {
        try {
            val modules = db.getAllModules()
            call.respond(HttpStatusCode.OK, modules)
        } catch (e: Exception){
            call.respond(HttpStatusCode.Conflict, emptyList<ModuleItem>())
        }
    }

}