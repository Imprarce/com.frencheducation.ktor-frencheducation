package com.frencheducation.plugins

import com.frencheducation.authentication.JwtService
import com.frencheducation.authentication.hash
import com.frencheducation.repository.*
import com.frencheducation.routes.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {

    val db = UserRepository()
    val jwtService = JwtService()
    val hashFunction = { s: String -> hash(s) }

    routing {

        get("/"){
            call.respond("Hello world!")
        }

        CommentsRoutes(CommentRepository())
        CommutinyRoutes(CommunityRepository())
        DictionaryRoutes(DictionaryRepository())
        FavoriteWordsRoutes(FavoriteWordsRepository())
        ModuleProgressRoutes(ModuleProgressRepository())
        ModuleRoutes(ModuleRepository())
        ModuleTasksRoutes(ModuleTasksRepository())
        TaskCompletedRoutes(TaskCompletedRepository())
        TaskRoutes(TaskRepository())
        UserRoutes(db, jwtService, hashFunction)
        VideoRoutes(VideoRepository())


    }
}
