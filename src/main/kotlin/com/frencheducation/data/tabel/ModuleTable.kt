package com.frencheducation.data.tabel

import com.frencheducation.data.tabel.TaskTable.autoIncrement
import org.jetbrains.exposed.sql.Table

object ModuleTable : Table("module") {
    var idModule = integer("id_module").autoIncrement()
    var imageUrl = varchar("image_url", 300)
    var moduleName = varchar("module_name", 60)
    var moduleLevel = integer("module_level")

    override val primaryKey: PrimaryKey = PrimaryKey(idModule)
}