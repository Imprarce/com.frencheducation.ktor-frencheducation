package com.frencheducation.routes

import com.frencheducation.data.model.SimpleResponse
import com.frencheducation.data.model.task.Task
import com.frencheducation.repository.TaskRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.TaskRoutes(
    db: TaskRepository
) {
    post("v1/tasks/create") {
        val task = try {
            call.receive<Task>()
        } catch (e: Exception){
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Пропущены необходимые параметры"))
            return@post
        }

        try {
            db.addTask(task)
            call.respond(HttpStatusCode.OK, SimpleResponse(true, "Задание успешно добавлено"))
        } catch (e: Exception){
            call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Возникла какая-то проблема"))
        }
    }

    post("v1/tasks/update"){
        val task = try {
            call.receive<Task>()
        } catch (e: Exception){
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Пропущены необходимые параметры"))
            return@post
        }

        try {
            db.updateTask(task, task.idTask)
            call.respond(HttpStatusCode.OK, SimpleResponse(true, "Задание успешно обновлено"))
        } catch (e: Exception){
            call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Возникла какая-то проблема"))
        }
    }

    delete("v1/tasks/delete") {
        val taskId = try {
            call.request.queryParameters["id"]!!
        } catch (e: Exception){
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "id написан неверно"))
            return@delete
        }

        try{
            db.deleteTask(taskId.toInt())
            call.respond(HttpStatusCode.OK, SimpleResponse(true, "Задание успешно удалено"))
        } catch (e: Exception){
            call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Возникла какая-то проблема"))
        }
    }

    get("v1/tasks/get") {
        val taskList = try {
            call.request.queryParameters.getAll("listTasks")?.map { it.toInt() } ?: emptyList()
        } catch (e: Exception){
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Пропущены необходимые параметры"))
            return@get
        }

        try {
            val tasks = db.getAllTasks(taskList)
            call.respond(HttpStatusCode.OK, tasks)
        } catch (e: Exception){
            call.respond(HttpStatusCode.Conflict, emptyList<Task>())
        }
    }

    get("v1/task/get") {
        val taskId = try {
            call.request.queryParameters["id_task"]!!
        } catch (e: Exception){
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "id написан неверно"))
            return@get
        }

        try {
            val task = db.getTaskById(taskId.toInt())
            if(task != null){
                call.respond(HttpStatusCode.OK, task)
            } else {
                call.respond(HttpStatusCode.BadRequest, "Задание не найдено")
            }
        } catch (e: Exception){
            call.respond(HttpStatusCode.Conflict, emptyList<Task>())
        }
    }

}