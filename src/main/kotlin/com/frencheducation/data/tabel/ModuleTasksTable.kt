package com.frencheducation.data.tabel

import com.frencheducation.data.tabel.TaskCompletedTable.references
import org.jetbrains.exposed.sql.Table

object ModuleTasksTable : Table("module_tasks") {
    var idModule = integer("id_module").references(ModuleTable.idModule)
    var idTask = integer("id_task").references(TaskTable.idTask)

    override val primaryKey: PrimaryKey = PrimaryKey(idModule, idTask)
}
