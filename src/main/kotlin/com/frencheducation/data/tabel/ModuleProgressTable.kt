package com.frencheducation.data.tabel

import com.frencheducation.data.tabel.TaskCompletedTable.references
import org.jetbrains.exposed.sql.Table

object ModuleProgressTable : Table("module_progress") {
    var idUser = integer("id_user").references(UserTable.idUser)
    var idModule = integer("id_module").references(ModuleTable.idModule)
    var moduleProgress = integer("module_progress")

    override val primaryKey: PrimaryKey = PrimaryKey(idUser, idModule)
}