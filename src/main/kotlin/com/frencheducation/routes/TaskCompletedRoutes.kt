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
            val idUser = call.principal<User>()!!.idUser
            val tasksCompleted = db.getAllTasksCompleted(idUser)
            call.respond(HttpStatusCode.OK, tasksCompleted)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, emptyList<TaskCompleted>())
        }
    }

}