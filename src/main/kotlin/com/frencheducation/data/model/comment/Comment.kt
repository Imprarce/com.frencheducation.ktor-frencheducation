package com.frencheducation.data.model.comment

data class Comment(
    var idComment: Int,
    var idUser: Int,
    var idCommunity: Int,
    var userImage: String,
    var userName: String,
    var rating: Int,
    var message: String
)
