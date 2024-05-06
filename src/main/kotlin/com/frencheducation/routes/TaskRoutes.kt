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
        try {
            val tasks = db.getAllTasks()
            call.respond(HttpStatusCode.OK, tasks)
        } catch (e: Exception){
            call.respond(HttpStatusCode.Conflict, emptyList<Task>())
        }
    }

}