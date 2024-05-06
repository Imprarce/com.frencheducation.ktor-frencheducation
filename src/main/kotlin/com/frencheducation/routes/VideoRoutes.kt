package com.frencheducation.routes

import com.frencheducation.data.model.SimpleResponse
import com.frencheducation.data.model.video.Video
import com.frencheducation.repository.VideoRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.VideoRoutes(
    db: VideoRepository
) {
    post("v1/video/create") {
        val video = try {
            call.receive<Video>()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Пропущены необходимые параметры"))
            return@post
        }

        try {
            db.addVideo(video)
            call.respond(HttpStatusCode.OK, SimpleResponse(true, "Видео успешно добавлено"))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Возникла какая-то проблема"))
        }
    }

    post("v1/video/update") {
        val video = try {
            call.receive<Video>()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Пропущены необходимые параметры"))
            return@post
        }

        try {
            db.updateVideo(video, video.videoId)
            call.respond(HttpStatusCode.OK, SimpleResponse(true, "Видео успешно обновлено"))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Возникла какая-то проблема"))
        }
    }

    delete("v1/video/delete") {
        val videoId = try {
            call.request.queryParameters["id"]!!
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "id написан неверно"))
            return@delete
        }

        try {
            db.deleteVideo(videoId.toInt())
            call.respond(HttpStatusCode.OK, SimpleResponse(true, "Видео успешно удалено"))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Возникла какая-то проблема"))
        }
    }

    get("v1/video/get") {
        try {
            val videos = db.getAllVideos()
            call.respond(HttpStatusCode.OK, videos)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, emptyList<Video>())
        }
    }

}