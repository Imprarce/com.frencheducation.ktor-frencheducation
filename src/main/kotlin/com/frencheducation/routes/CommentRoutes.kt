package com.frencheducation.routes

import com.frencheducation.data.model.SimpleResponse
import com.frencheducation.data.model.comment.Comment
import com.frencheducation.repository.CommentRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.CommentsRoutes(
    db: CommentRepository
) {
    post("v1/comment/create") {
        val comment = try {
            call.receive<Comment>()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Пропущены необходимые параметры"))
            return@post
        }

        try {
            db.addComment(comment)
            call.respond(HttpStatusCode.OK, SimpleResponse(true, "Комментарий успешно добавлен"))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Возникла какая-то проблема"))
        }
    }

    post("v1/comment/update") {
        val commentId = try {
            call.request.queryParameters["id_comment"]!!
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "id написан неверно"))
            return@post
        }

        try {
//            db.updateComment(comment, comment.idComment)
            call.respond(HttpStatusCode.OK, SimpleResponse(true, "Комментарий успешно обновлен"))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Возникла какая-то проблема"))
        }
    }

    delete("v1/comment/delete") {
        val commentId = try {
            call.request.queryParameters["id_comment"]!!
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "id написан неверно"))
            return@delete
        }

        try {
            db.deleteComment(commentId.toInt())
            call.respond(HttpStatusCode.OK, SimpleResponse(true, "Комментарий успешно удален"))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Возникла какая-то проблема"))
        }
    }

    get("v1/comment/get") {
        try {
            val communityId = try {
                call.request.queryParameters["id_community"]!!
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "id написан неверно"))
                return@get
            }
            val comments = db.getAllComments(communityId.toInt())
            call.respond(HttpStatusCode.OK, comments)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Возникла какая-то проблема"))
        }
    }

}