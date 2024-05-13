package com.frencheducation.data.model.community

import java.time.LocalDateTime

data class CommunityRequest(
    var idCommunity: Int,
    var idUser: Int,
    var userImage: String,
    var userName: String,
    var title: String,
    var rating: Int,
    var view: Int,
    var hasProblemResolve: Boolean,
    var description: String
)
