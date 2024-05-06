package com.frencheducation.routes

import com.frencheducation.data.model.SimpleResponse
import com.frencheducation.data.model.community.Community
import com.frencheducation.repository.CommunityRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.CommutinyRoutes(
    db: CommunityRepository
) {
    post("v1/community/create") {
        val community = try {
            call.receive<Community>()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Пропущены необходимые параметры"))
            return@post
        }

        try {
            db.addCommunity(community)
            call.respond(HttpStatusCode.OK, SimpleResponse(true, "Обсуждение успешно добавлено"))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Возникла какая-то проблема"))
        }
    }

    post("v1/community/update") {
        val community = try {
            call.receive<Community>()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Пропущены необходимые параметры"))
            return@post
        }

        try {
            db.updateCommunity(community, community.idCommunity)
            call.respond(HttpStatusCode.OK, SimpleResponse(true, "Обсуждение успешно обновлено"))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Возникла какая-то проблема"))
        }
    }

    delete("v1/community/delete") {
        val communityId = try {
            call.request.queryParameters["id"]!!
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "id написан неверно"))
            return@delete
        }

        try {
            db.deleteCommunity(communityId.toInt())
            call.respond(HttpStatusCode.OK, SimpleResponse(true, "Обсуждение успешно удалено"))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Возникла какая-то проблема"))
        }
    }

    get("v1/community/get") {
        try {
            val communities = db.getAllCommunities()
            call.respond(HttpStatusCode.OK, communities)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, emptyList<Community>())
        }
    }

}