package com.frencheducation.repository

import com.frencheducation.data.model.favorite_words.FavoriteWords
import com.frencheducation.data.model.module.ModuleItem
import com.frencheducation.data.model.module_progress.ModuleProgress
import com.frencheducation.data.tabel.FavoriteWordsTable
import com.frencheducation.data.tabel.ModuleProgressTable
import com.frencheducation.data.tabel.ModuleTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class ModuleProgressRepository {
    suspend fun addModuleProgress(moduleProgressItem: ModuleProgress) {
        DatabaseFactory.dbQuery {
            ModuleProgressTable.insert { moduleProgressTable ->
                moduleProgressTable[idModule] = moduleProgressItem.idModule
                moduleProgressTable[idUser] = moduleProgressItem.idUser
                moduleProgressTable[moduleProgress] = moduleProgressItem.progress
            }
        }
    }

    suspend fun getAllModulesProgress(id_user: Int): List<ModuleProgress> {
        return DatabaseFactory.dbQuery {
            ModuleProgressTable.select{
                ModuleProgressTable.idUser.eq(id_user)
            }.mapNotNull { rowToModuleProgress(it) }
        }
    }

    suspend fun updateModuleProgress(moduleProgressItem: ModuleProgress, id_module: Int, id_user: Int) {
        DatabaseFactory.dbQuery {
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