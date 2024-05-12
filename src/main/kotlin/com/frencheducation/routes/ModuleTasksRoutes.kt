package com.frencheducation.routes

import com.frencheducation.data.model.SimpleResponse
import com.frencheducation.data.model.module_tasks.ModuleTasks
import com.frencheducation.data.model.task.Task
import com.frencheducation.data.model.task_completed.TaskCompleted
import com.frencheducation.data.model.video.Video
import com.frencheducation.repository.ModuleTasksRepository
import com.frencheducation.repository.TaskCompletedRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.ModuleTasksRoutes(
    db: ModuleTasksRepository
) {
    post("v1/module_tasks/create") {
        val moduleTasks = try {
            call.receive<ModuleTasks>()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Пропущены необходимые параметры"))
            return@post
        }

        try {
            db.addModuleTasks(moduleTasks)
            call.respond(HttpStatusCode.OK, SimpleResponse(true, "Модуль с заданиями успешно добавлен"))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Возникла какая-то проблема"))
        }
    }

    post("v1/module_tasks/update"){
        val moduleTasks = try {
            call.receive<ModuleTasks>()
        } catch (e: Exception){
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Пропущены необходимые параметры"))
            return@post
        }

        try {
            db.updateModuleTasks(moduleTasks, moduleTasks.idModule, moduleTasks.idTask)
            call.respond(HttpStatusCode.OK, SimpleResponse(true, "Модуль с заданиями успешно обновлен"))
        } catch (e: Exception){
            call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Возникла какая-то проблема"))
        }
    }

    delete("v1/module_tasks/delete") {
        val moduleTasksId = try {
            call.request.queryParameters["id_module"]!!
            call.request.queryParameters["id_task"]!!
        } catch (e: Exception){
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "id написаны неверно"))
            return@delete
        }

        try{
            db.deleteModuleTasks(moduleTasksId[0].toInt(), moduleTasksId[1].toInt())
            call.respond(HttpStatusCode.OK, SimpleResponse(true, "Модуль с заданиями успешно удален"))
        } catch (e: Exception){
            call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Возникла какая-то проблема"))
        }
    }

    get("v1/module_tasks/get") {
        try {
            val idModule = call.request.queryParameters["id_module"]

            if (idModule.isNullOrBlank()) {
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Модуль не найден"))
                return@get
            }

            val moduleTasks = db.getModuleTasksById(idModule.toInt())

            call.respond(HttpStatusCode.OK, moduleTasks)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, emptyList<ModuleTasks>())
        }
    }

}