package com.frencheducation.routes

import com.frencheducation.data.model.SimpleResponse
import com.frencheducation.data.model.community.Community
import com.frencheducation.data.model.community.CommunityRequest
import com.frencheducation.data.model.user.LocalDateTimeTypeAdapter
import com.frencheducation.repository.CommunityRepository
import com.google.gson.GsonBuilder
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.time.LocalDateTime

fun Route.CommutinyRoutes(
    db: CommunityRepository
) {

    post("v1/community/create") {
        val communityRequest = try {
            call.receive<CommunityRequest>()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, e.message ?: "Пропущены некоторые параметры"))
            return@post
        }

        try {
            val community = Community(
                idCommunity = communityRequest.idCommunity,
                idUser = communityRequest.idUser,
                userImage = communityRequest.userImage,
                userName = communityRequest.userName,
                title = communityRequest.title,
                rating = communityRequest.rating,
                view = communityRequest.view,
                hasProblemResolve = communityRequest.hasProblemResolve,
                description = communityRequest.description,
                createTime = LocalDateTime.now(),
                lastChange = LocalDateTime.now()
            )

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
            call.request.queryParameters["id_community"]!!
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

    get("v1/communities/get") {
        try {
            val communities = db.getAllCommunities()
            val gson = GsonBuilder()
                .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeTypeAdapter())
                .create()
            val jsonString = gson.toJson(communities)
            call.respond(HttpStatusCode.OK, jsonString)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Возникла какая-то проблема"))
        }
    }

    get("v1/community/get") {
        val communityId = try {
            call.request.queryParameters["id_community"]!!
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "id написан неверно"))
            return@get
        }

        try {
            val community = db.getCommunityById(communityId.toInt())
            val gson = GsonBuilder()
                .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeTypeAdapter())
                .create()
            val jsonString = gson.toJson(community)
            call.respond(HttpStatusCode.OK, jsonString)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Возникла какая-то проблема"))
        }
    }

}