package com.frencheducation.data.model.user

import java.time.LocalDateTime

data class User(
    var id: Int,
    var email: String,
    var hashPassword: String,
    var userName: String,
    var imageUrl: String,
    var dateCreateAcc: LocalDateTime
)