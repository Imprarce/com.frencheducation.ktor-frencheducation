package com.frencheducation.repository

import com.frencheducation.data.model.module_progress.ModuleProgress
import com.frencheducation.data.tabel.ModuleProgressTable
import com.frencheducation.repository.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.*

class ModuleProgressRepository {
    suspend fun addModuleProgress(moduleProgressItem: ModuleProgress) {
        dbQuery {
            ModuleProgressTable.insert { moduleProgressTable ->
                moduleProgressTable[idModule] = moduleProgressItem.idModule
                moduleProgressTable[idUser] = moduleProgressItem.idUser
                moduleProgressTable[moduleProgress] = moduleProgressItem.progress
            }
        }
    }

    suspend fun getModuleProgress(id_user: Int, id_module: Int) = dbQuery {
        ModuleProgressTable.select {
            ModuleProgressTable.idUser.eq(id_user) and ModuleProgressTable.idModule.eq(id_module)
        }
            .mapNotNull { rowToModuleProgress(it) }
            .singleOrNull()
    }

    suspend fun updateModuleProgress(moduleProgressItem: ModuleProgress, id_module: Int, id_user: Int) {
        dbQuery {
            ModuleProgressTable.update(
                where = {
                    ModuleProgressTable.idModule.eq(id_module) and ModuleProgressTable.idUser.eq(id_user)
                }
            ) { moduleProgressTable ->
                moduleProgressTable[moduleProgress] = moduleProgressItem.progress
            }
        }
    }

    private fun rowToModuleProgress(row: ResultRow?): ModuleProgress? {
        if (row == null) {
            return null
        }

        return ModuleProgress(
            idModule = row[ModuleProgressTable.idModule],
            idUser = row[ModuleProgressTable.idUser],
            progress = row[ModuleProgressTable.moduleProgress]
        )
    }
}