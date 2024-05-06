package com.frencheducation.data.tabel


import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object UserTable : Table("user") {
    var idUser = integer("id_user").autoIncrement()
    var email = varchar("email", 50)
    var hashPassword = varchar("password", 512)
    var userName = varchar("user_name", 50)
    var imageUrl = varchar("image_url", 200)
    var dateCreateAcc = datetime("date_create_acc")

    override val primaryKey: PrimaryKey = PrimaryKey(idUser)
    init {
        index(true, email)
    }
}