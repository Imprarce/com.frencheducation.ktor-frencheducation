package com.frencheducation.routes

import com.frencheducation.data.model.SimpleResponse
import com.frencheducation.data.model.task_completed.TaskCompleted
import com.frencheducation.data.model.user.User
import com.frencheducation.data.model.video.Video
import com.frencheducation.repository.TaskCompletedRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.TaskCompletedRoutes(
    db: TaskCompletedRepository
) {
    post("v1/task_completed/create") {
        val taskCompleted = try {
            call.receive<TaskCompleted>()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Пропущены необходимые параметры"))
            return@post
        }

        try {
            db.addTaskCompleted(taskCompleted)
            call.respond(HttpStatusCode.OK, SimpleResponse(true, "Сделанное задание успешно добавлено"))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Возникла какая-то проблема"))
        }
    }

    get("v1/task_completed/get") {
        try {
            val idUser = call.request.queryParameters["id_user"]
            val idTask = call.request.queryParameters["id_task"]

            if (idUser.isNullOrBlank()) {
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Пользователь не найден"))
                return@get
            }

            if (idTask.isNullOrBlank()) {
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Задание не найдено"))
                return@get
            }

            val taskCompleted = db.getTaskCompleted(idUser.toInt(), idTask.toInt())

            if (taskCompleted == null) {
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Задание не найдено"))
                return@get
            }

            call.respond(HttpStatusCode.OK, taskCompleted)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, "")
        }
    }

    get("v1/tasks_completed_by_user/get") {
        try {
            val idUser = call.request.queryParameters["id_user"]

            if (idUser.isNullOrBlank()) {
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Пользователь не найден"))
                return@get
            }


            val taskCompleted = db.getTasksCompletedByUser(idUser.toInt())

            if (taskCompleted == null) {
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Пользователь ничего еще не сделал"))
                return@get
            }

            call.respond(HttpStatusCode.OK, taskCompleted)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, "")
        }
    }
}