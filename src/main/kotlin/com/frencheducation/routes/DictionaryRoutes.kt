package com.frencheducation.routes

import com.frencheducation.data.model.SimpleResponse
import com.frencheducation.data.model.dictionary.Dictionary
import com.frencheducation.repository.DictionaryRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.DictionaryRoutes(
    db: DictionaryRepository
) {
    post("v1/dictionary/create") {
        val dictionary = try {
            call.receive<Dictionary>()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Пропущены необходимые параметры"))
            return@post
        }

        try {
            db.addWord(dictionary)
            call.respond(HttpStatusCode.OK, SimpleResponse(true, "Слово успешно добавлено"))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Возникла какая-то проблема"))
        }
    }

    post("v1/dictionary/update") {
        val dictionary = try {
            call.receive<Dictionary>()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Пропущены необходимые параметры"))
            return@post
        }

        try {
            db.updateWord(dictionary, dictionary.idWord)
            call.respond(HttpStatusCode.OK, SimpleResponse(true, "Слово успешно обновлено"))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Возникла какая-то проблема"))
        }
    }

    delete("v1/dictionary/delete") {
        val wordId = try {
            call.request.queryParameters["id"]!!
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "id написан неверно"))
            return@delete
        }

        try {
            db.deleteWord(wordId.toInt())
            call.respond(HttpStatusCode.OK, SimpleResponse(true, "Слово успешно удалено"))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Возникла какая-то проблема"))
        }
    }

    get("v1/dictionary/get") {
        try {
            val words = db.getAllWords()
            call.respond(HttpStatusCode.OK, words)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, emptyList<Dictionary>())
        }
    }

}