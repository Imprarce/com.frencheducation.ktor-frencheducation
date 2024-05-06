package com.frencheducation.data.model.community

import java.time.LocalDateTime

data class Community(
    var idCommunity: Int,
    var idUser: Int,
    var title: String,
    var rating: Int,
    var view: Int,
    var createTime: LocalDateTime,
    var lastChange: LocalDateTime,
    var hasProblemResolve: Boolean,
    var description: String
)
