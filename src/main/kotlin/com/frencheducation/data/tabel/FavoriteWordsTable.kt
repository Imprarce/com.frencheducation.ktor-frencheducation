package com.frencheducation.data.tabel

import com.frencheducation.data.tabel.ModuleTasksTable.references
import com.frencheducation.data.tabel.TaskCompletedTable.references
import org.jetbrains.exposed.sql.Table

object FavoriteWordsTable : Table("favorite_word") {
    var idUser = integer("id_user").references(UserTable.idUser)
    var idWord = integer("id_word").references(DictionaryTable.idWord)

    override val primaryKey: PrimaryKey = PrimaryKey(idUser, idWord)
}