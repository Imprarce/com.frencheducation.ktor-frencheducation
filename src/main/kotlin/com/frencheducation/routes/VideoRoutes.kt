package com.frencheducation.routes

import com.frencheducation.data.model.SimpleResponse
import com.frencheducation.data.model.user.LocalDateTimeTypeAdapter
import com.frencheducation.data.model.video.Video
import com.frencheducation.repository.VideoRepository
import com.google.gson.GsonBuilder
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File
import java.time.LocalDateTime

fun Route.VideoRoutes(
    db: VideoRepository
) {
    post("v1/video/create") {

        val multipart = call.receiveMultipart()


        var videoFields: Video? = null
        var fileName: String? = null


        multipart.forEachPart { part ->
            when (part) {
                is PartData.FormItem -> {

                    val fieldName = part.name
                    val fieldValue = part.value
                    when (fieldName) {
                        "videoId" -> videoFields = videoFields?.copy(videoId = fieldValue.toInt()) ?: Video(videoId = fieldValue.toInt(), userId = 0, rating = 0, view = 0, description = "", title = "", videoFile = "")
                        "userId" -> videoFields = videoFields?.copy(userId = fieldValue.toInt()) ?: Video(videoId = 0, userId = fieldValue.toInt(), rating = 0, view = 0, description = "", title = "", videoFile = "")
                        "title" -> videoFields = videoFields?.copy(title = fieldValue) ?: Video(videoId = 0, userId = 0, rating = 0, view = 0, description = "", title = fieldValue, videoFile = "")
                        "description" -> videoFields = videoFields?.copy(description = fieldValue) ?: Video(videoId = 0, userId = 0, rating = 0, view = 0, description = fieldValue, title = "", videoFile = "")
                    }
                }
                is PartData.FileItem -> {
                    fileName = part.originalFileName ?: "unkown"
                    val file = File("src\\main\\resources\\videos\\$fileName")
                    part.streamProvider().use { input ->
                        file.outputStream().buffered().use { output ->
                            input.copyTo(output)
                        }
                    }
                }

                else -> {}
            }
            part.dispose()
        }

        val videoUrl = "${Constants.BASE_URL}/$fileName"

        videoFields = videoFields?.copy(videoFile = videoUrl) ?: Video(videoId = 0, userId = 0, rating = 0, view = 0, description = "", title = "", videoFile = videoUrl)

        if (videoFields != null) {
            try {
                db.addVideo(videoFields!!)
                call.respond(HttpStatusCode.OK, SimpleResponse(true, "Видео успешно добавлено"))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Возникла какая-то проблема"))
            }
        } else {
            File("${Constants.USER_IMAGES_PATH}/$fileName").delete()
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Пропущены необходимые параметры"))
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

    get("/videos/{name}") {
        val filename = call.parameters["name"]!!
        val file = File("src\\main\\resources\\videos\\$filename")
        if(file.exists()) {
            call.respondFile(file)
        }
        else call.respond(HttpStatusCode.NotFound)
    }

    delete("v1/video/delete") {
        val videoId = try {
            call.request.queryParameters["id_video"]!!
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

    get("v1/videos/get") {
        try {
            val videos = db.getAllVideos()
            call.respond(HttpStatusCode.OK, videos)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Возникла какая-то проблема"))
        }
    }

    get("v1/video/get") {
        val videoId = try {
            call.request.queryParameters["id_video"]!!
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "id написан неверно"))
            return@get
        }

        try {
            val video = db.getVideoById(videoId.toInt())

            call.respond(HttpStatusCode.OK, video!!)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Возникла какая-то проблема"))
        }
    }

}