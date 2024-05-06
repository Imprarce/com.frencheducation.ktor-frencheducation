package com.frencheducation.data.tabel

import com.frencheducation.data.tabel.CommunityTable.autoIncrement
import com.frencheducation.data.tabel.CommunityTable.references
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object CommentTable : Table("comment") {
    var idComment = integer("id_comment").autoIncrement()
    var idUser = integer("id_user").references(UserTable.idUser)
    var idCommunity = integer("id_community").references(CommunityTable.idCommunity)
    var rating = integer("rating")
    var message = varchar("message", 600)

    override val primaryKey: PrimaryKey = PrimaryKey(idComment, idUser, idCommunity)
}