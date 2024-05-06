package com.frencheducation.data.tabel

import com.frencheducation.data.tabel.VideoTable.references
import org.jetbrains.exposed.sql.Table

object TaskCompletedTable : Table("task_completed") {
    var idUser = integer("id_user").references(UserTable.idUser)
    var idTask = integer("id_task").references(TaskTable.idTask)

    override val primaryKey: PrimaryKey = PrimaryKey(idUser, idTask)
}