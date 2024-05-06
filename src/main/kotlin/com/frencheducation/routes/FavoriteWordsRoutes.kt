package com.frencheducation.routes

import com.frencheducation.data.model.SimpleResponse
import com.frencheducation.data.model.favorite_words.FavoriteWords
import com.frencheducation.data.model.module_progress.ModuleProgress
import com.frencheducation.data.model.module_tasks.ModuleTasks
import com.frencheducation.data.model.user.User
import com.frencheducation.repository.FavoriteWordsRepository
import com.frencheducation.repository.ModuleProgressRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.FavoriteWordsRoutes(
    db: FavoriteWordsRepository
) {
    post("v1/favorite_words/create") {
        val favoriteWords = try {
            call.receive<FavoriteWords>()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Пропущены необходимые параметры"))
            return@post
        }

        try {
            db.addFavoriteWord(favoriteWords)
            call.respond(HttpStatusCode.OK, SimpleResponse(true, "Избранное слово успешно добавлено"))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Возникла какая-то проблема"))
        }
    }

    delete("v1/favorite_words/delete") {
        val favoriteWordsIds = try {
            call.request.queryParameters["id_user"]!!
            call.request.queryParameters["id_word"]!!
        } catch (e: Exception){
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "id написан неверно"))
            return@delete
        }

        try{
            db.deleteFavoriteWord(favoriteWordsIds[0].toInt(), favoriteWordsIds[1].toInt())
            call.respond(HttpStatusCode.OK, SimpleResponse(true, "Избранное слово успешно удалено"))
        } catch (e: Exception){
            call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Возникла какая-то проблема"))
        }
    }

    get("v1/favorite_words/get") {
        try {
            val idUser = call.principal<User>()!!.idUser
            val moduleTasks = db.getAllFavoriteWords(idUser)
            call.respond(HttpStatusCode.OK, moduleTasks)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, emptyList<ModuleTasks>())
        }
    }

}