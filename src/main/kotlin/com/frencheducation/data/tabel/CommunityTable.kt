package com.frencheducation.data.tabel

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object CommunityTable : Table("community") {
    var idCommunity = integer("id_community").autoIncrement()
    var idUser = integer("id_user").references(UserTable.idUser)
    var title = varchar("title", 60)
    var rating = integer("rating")
    var view = integer("view")
    var createTime = datetime("create_time")
    var lastChange = datetime("last_change")
    var hasProblemResolve = bool("has_problem_resolve")
    var description = varchar("description", 600)

    override val primaryKey: PrimaryKey = PrimaryKey(idCommunity, idUser)

    init {
        uniqueIndex(idCommunity)
    }
}