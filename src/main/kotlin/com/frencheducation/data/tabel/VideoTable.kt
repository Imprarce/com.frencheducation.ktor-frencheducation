package com.frencheducation.data.tabel

import com.frencheducation.data.tabel.UserTable.autoIncrement
import org.jetbrains.exposed.sql.Table

object VideoTable : Table("video") {
    var idVideo = integer("id_video").autoIncrement()
    var idUser = integer("id_user").references(UserTable.idUser)
    var rating = integer("rating")
    var view = integer("view")
    var description = varchar("description", 300)
    var title = varchar("title", 60)
    var videoFile = varchar("video_file", 300)

    override val primaryKey: PrimaryKey = PrimaryKey(idVideo)

}