package com.frencheducation.data.model.user

import io.ktor.server.auth.*
import java.time.LocalDateTime

data class User(
    var idUser: Int,
    var email: String,
    var hashPassword: String,
    var userName: String,
    var imageUrl: String,
    var dateCreateAcc: String
) : Principal