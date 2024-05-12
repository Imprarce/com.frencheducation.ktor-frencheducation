package com.frencheducation.repository

import com.frencheducation.data.model.module.ModuleItem
import com.frencheducation.data.model.module_tasks.ModuleTasks
import com.frencheducation.data.tabel.ModuleTable
import com.frencheducation.data.tabel.ModuleTasksTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class ModuleTasksRepository {
    suspend fun addModuleTasks(moduleTasks: ModuleTasks) {
        DatabaseFactory.dbQuery {
            ModuleTasksTable.insert { moduleTasksTable ->
                moduleTasksTable[idModule] = moduleTasks.idModule
                moduleTasksTable[idTask] = moduleTasks.idTask
            }
        }
    }

    suspend fun getAllModulesTasks(id_module: Int): List<ModuleTasks> {
        return DatabaseFactory.dbQuery {
            ModuleTasksTable.selectAll()
                .mapNotNull { rowToModuleTasks(it) }
        }
    }

    suspend fun updateModuleTasks(moduleTasks: ModuleTasks, id_module: Int, id_task: Int) {
        DatabaseFactory.dbQuery {
            ModuleTasksTable.update(
                where = {
                    ModuleTasksTable.idModule.eq(id_module) and ModuleTasksTable.idTask.eq(id_task)
                }
            ) { moduleTasksTable ->
                moduleTasksTable[idModule] = moduleTasks.idModule
                moduleTasksTable[idTask] = moduleTasks.idTask
            }
        }
    }

    suspend fun deleteModuleTasks(id_module: Int, id_task: Int) {
        DatabaseFactory.dbQuery {
            ModuleTasksTable.deleteWhere { ModuleTasksTable.idModule.eq(id_module) and ModuleTasksTable.idTask.eq(id_task) }
        }
    }

    suspend fun getModuleTasksById(id_module: Int) = DatabaseFactory.dbQuery {
        ModuleTasksTable.select { ModuleTasksTable.idModule.eq(id_module)}
            .map { rowToModuleTasks(it) }
    }

    private fun rowToModuleTasks(row: ResultRow?): ModuleTasks? {
        if (row == null) {
            return null
        }

        return ModuleTasks(
            idModule = row[ModuleTasksTable.idModule],
            idTask = row[ModuleTasksTable.idTask]
        )
    }
}