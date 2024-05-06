package com.frencheducation.repository

import com.frencheducation.data.model.module.ModuleItem
import com.frencheducation.data.tabel.ModuleTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class ModuleRepository {

    suspend fun addModule(moduleItem: ModuleItem) {
        DatabaseFactory.dbQuery {
            ModuleTable.insert { moduleTable ->
                moduleTable[imageUrl] = moduleItem.imageUrl
                moduleTable[moduleName] = moduleItem.moduleName
                moduleTable[moduleLevel] = moduleItem.moduleLevel
            }
        }
    }

    suspend fun getAllModules(): List<ModuleItem> {
        return DatabaseFactory.dbQuery {
            ModuleTable.selectAll()
                .mapNotNull { rowToModule(it) }
        }
    }

    suspend fun updateModule(moduleItem: ModuleItem, id: Int) {
        DatabaseFactory.dbQuery {
            ModuleTable.update(
                where = {
                    ModuleTable.idModule.eq(id)
                }
            ) { moduleTable ->
                moduleTable[imageUrl] = moduleItem.imageUrl
                moduleTable[moduleName] = moduleItem.moduleName
                moduleTable[moduleLevel] = moduleItem.moduleLevel
            }
        }
    }

    suspend fun deleteModule(id: Int) {
        DatabaseFactory.dbQuery {
            ModuleTable.deleteWhere { ModuleTable.idModule.eq(id) }
        }
    }

    suspend fun getModuleById(id: Int) = DatabaseFactory.dbQuery {
        ModuleTable.select { ModuleTable.idModule.eq(id) }
            .map { rowToModule(it) }
            .singleOrNull()
    }

    private fun rowToModule(row: ResultRow?): ModuleItem? {
        if (row == null) {
            return null
        }

        return ModuleItem(
            idModule = row[ModuleTable.idModule],
            imageUrl = row[ModuleTable.imageUrl],
            moduleName = row[ModuleTable.moduleName],
            moduleLevel = row[ModuleTable.moduleLevel],
        )
    }
}